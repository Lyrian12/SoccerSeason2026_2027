#!/usr/bin/env python3
"""Build idempotent 2026-27 squad inserts from Transfermarkt's public roster pages.

The output targets teams by league and name rather than the development database's
numeric IDs, so it can be re-run after a database reset.
"""

from __future__ import annotations

import html
import argparse
import re
import subprocess
import time
import urllib.parse
import urllib.request
from pathlib import Path


OUTPUT = Path("src/main/resources/2026_2027_squads.sql")
USER_AGENT = "Mozilla/5.0 (compatible; SoccerSeason squad importer)"
SEASON_ID = "2026"  # Transfermarkt's 2026/27 season identifier.

# The database uses a few French abbreviations while the public search uses the
# clubs' common English names.
SEARCH_NAMES = {
    "Ath. Bilbao": "Athletic Club Bilbao",
    "Augsbourg": "FC Augsburg",
    "Bologne": "Bologna FC 1909",
    "Côme": "Como 1907",
    "Deportivo": "Deportivo La Coruna",
    "Fribourg": "SC Freiburg",
    "Hambourg": "Hamburger SV",
    "Mönchengladbach": "Borussia Monchengladbach",
    "Naples": "SSC Napoli",
    "Nottm Forest": "Nottingham Forest",
    "Parme": "Parma Calcio 1913",
    "Paris-SG": "Paris Saint-Germain",
    "Santander": "Racing Santander",
    "Séville": "Sevilla FC",
    "Valence": "Valencia CF",
}

# Search results occasionally surface an "unknown" placeholder before the
# senior side. These direct paths keep those lookups deterministic.
CLUB_PATHS = {
    "Aston Villa": "/aston-villa/startseite/verein/405",
    "Crystal Palace": "/crystal-palace/startseite/verein/873",
    "Chelsea": "/fc-chelsea/startseite/verein/631",
    "Elche": "/fc-elche/startseite/verein/1531",
    "Monza": "/ac-monza/startseite/verein/2919",
    "Cagliari": "/cagliari-calcio/startseite/verein/1390",
    "Juventus": "/juventus-turin/startseite/verein/506",
    "Torino": "/fc-turin/startseite/verein/416",
    "Lecce": "/us-lecce/startseite/verein/1005",
    "Venezia": "/fc-venezia/startseite/verein/607",
    "Auxerre": "/aj-auxerre/startseite/verein/290",
    "Cologne": "/1-fc-koln/startseite/verein/3",
    "Werder": "/werder-bremen/startseite/verein/86",
    "Angers": "/sco-angers/startseite/verein/1420",
    "Monaco": "/as-monaco/startseite/verein/162",
    "Troyes": "/estac-troyes/startseite/verein/1095",
    "Le Mans": "/le-mans-fc/startseite/verein/1164",
    "Lille": "/losc-lille/startseite/verein/1082",
    "Nice": "/ogc-nizza/startseite/verein/417",
    "Lyon": "/olympique-lyon/startseite/verein/1041",
    "Lens": "/rc-lens/startseite/verein/995",
    "Rennes": "/stade-rennais-fc/startseite/verein/273",
    "Everton": "/fc-everton/startseite/verein/29",
    "Villarreal": "/fc-villarreal/startseite/verein/1050",
}

POSITION_MAP = {
    "Goalkeeper": "GOALKEEPER",
    "Centre-Back": "CENTER_BACK",
    "Center-Back": "CENTER_BACK",
    "Right-Back": "RIGHT_BACK",
    "Left-Back": "LEFT_BACK",
    "Defensive Midfield": "DEFENSIVE_MIDFIELDER",
    "Central Midfield": "CENTRAL_MIDFIELDER",
    "Attacking Midfield": "ATTACKING_MIDFIELDER",
    "Right Winger": "RIGHT_WINGER",
    "Left Winger": "LEFT_WINGER",
    "Centre-Forward": "STRIKER",
    "Center-Forward": "STRIKER",
    "Second Striker": "STRIKER",
    "Left Midfield": "CENTRAL_MIDFIELDER",
    "Right Midfield": "CENTRAL_MIDFIELDER",
    "Midfielder": "CENTRAL_MIDFIELDER",
}


def fetch(url: str) -> str:
    request = urllib.request.Request(url, headers={"User-Agent": USER_AGENT})
    with urllib.request.urlopen(request, timeout=30) as response:
        return response.read().decode("utf-8", errors="replace")


def empty_teams() -> list[tuple[str, str]]:
    sql = """SELECT l.name, t.name FROM teams t JOIN leagues l ON l.id=t.league_id
             WHERE NOT EXISTS (SELECT 1 FROM players p WHERE p.team_id=t.id)
             ORDER BY l.id, t.id"""
    result = subprocess.run(
        ["docker", "exec", "football_db_container", "psql", "-U", "postgres", "-d", "football", "-At", "-F", "|", "-c", sql],
        check=True,
        capture_output=True,
        text=True,
    )
    return [tuple(line.split("|", 1)) for line in result.stdout.splitlines() if line]


def club_path(query: str) -> str:
    page = fetch("https://www.transfermarkt.com/schnellsuche/ergebnis/schnellsuche?query=" + urllib.parse.quote(query))
    match = re.search(r'href="(/[^"]+/startseite/verein/\d+)', page)
    if not match:
        raise RuntimeError(f"No Transfermarkt club match for {query!r}")
    return match.group(1)


def roster(path: str) -> list[tuple[str, str]]:
    page = fetch(f"https://www.transfermarkt.com{path}/saison_id/{SEASON_ID}")
    records = re.findall(
        r'profil/spieler/\d+">\s*([^<]+?)\s*</a>.*?</tr>\s*<tr>\s*<td>\s*([^<]+?)\s*</td>',
        page,
        flags=re.DOTALL,
    )
    players = []
    for raw_name, raw_position in records:
        name = re.sub(r"\s+", " ", html.unescape(re.sub(r"<[^>]+>", "", raw_name))).strip()
        position = re.sub(r"\s+", " ", html.unescape(re.sub(r"<[^>]+>", "", raw_position))).strip()
        mapped = POSITION_MAP.get(position)
        if name and mapped:
            players.append((name, mapped))
    if len(players) < 15:
        raise RuntimeError(f"Only found {len(players)} usable players at {path}")
    return players


def sql_string(value: str) -> str:
    return "'" + value.replace("'", "''") + "'"


def split_name(full_name: str) -> tuple[str, str]:
    parts = full_name.rsplit(" ", 1)
    return (parts[0], parts[1]) if len(parts) == 2 else (full_name, full_name)


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--offset", type=int, default=0)
    parser.add_argument("--limit", type=int)
    args = parser.parse_args()
    teams = empty_teams()
    selected = teams[args.offset : args.offset + args.limit if args.limit else None]
    statements = []
    if args.offset == 0:
        header = [
            "-- Generated from Transfermarkt public 2026/27 roster pages.",
            "-- Safe to run multiple times: existing player/team combinations are skipped.",
            "",
        ]
        OUTPUT.write_text("\n".join(header) + "\n", encoding="utf-8")
    for index, (league, team) in enumerate(selected, start=args.offset + 1):
        query = SEARCH_NAMES.get(team, team)
        path = CLUB_PATHS.get(team, club_path(query))
        players = roster(path)
        team_statements = [f"-- {league}: {team} ({len(players)} players)"]
        for full_name, position in players:
            first_name, surname = split_name(full_name)
            team_statements.append(
                "INSERT INTO players (name, surname, playerposition, team_id) "
                f"SELECT {sql_string(first_name)}, {sql_string(surname)}, '{position}', t.id "
                "FROM teams t JOIN leagues l ON l.id = t.league_id "
                f"WHERE t.name = {sql_string(team)} AND l.name = {sql_string(league)} "
                "AND NOT EXISTS (SELECT 1 FROM players p WHERE p.team_id = t.id "
                f"AND p.name = {sql_string(first_name)} AND p.surname = {sql_string(surname)});"
            )
        team_statements.append("")
        with OUTPUT.open("a", encoding="utf-8") as output:
            output.write("\n".join(team_statements) + "\n")
        print(f"[{index}/{len(teams)}] {league} — {team}: {len(players)} players", flush=True)
        time.sleep(0.4)
    print(f"Wrote {OUTPUT}", flush=True)


if __name__ == "__main__":
    main()
