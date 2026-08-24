--- 
#            Soccer_2026/2027

---
> this is an overview project on the 2026/2027 squads and teams in the big five leagues in europe.
>  <img src="/src/main/resources/static/images/premier-league.svg" width="25px" height="25px">
>  <img src="/src/main/resources/static/images/ligue1.jpeg" width="25px" height="25px">
> <img src="/src/main/resources/static/images/LaLiga.svg" width="25px" height="25px">
>  <img src="/src/main/resources/static/images/serieA.svg" width="25px" height="25px">
>  <img src="https://upload.wikimedia.org/wikipedia/en/d/df/Bundesliga_logo_%282017%29.svg" width ="25px" height="25px">

`the project is build with spring_boot on the backend and thymeleaf on the 
frontend.`

### Dependencies: 
`lombok:` help for generates all the boring stuff in our class such as
1. getters
2. setters
3. constructors with arguments
4. constructors without arguments
5. hash map code
6. override the `ToString()` method

`Spring Data Jpa:`help with the database with the hibernate orm for mapping.

`Spring Webmvc:` help to build a web app following the model,view,controller patterns.

`postgresql:` dependence add to use a postgresql database.

`docker:` use a compose file to surround my database in it.

## how to get started


1. install **jdk 17 or Above**.    https://adoptium.net
2. install **docker desktop**. https://docker.com
3. install **Git**. https://git-scm.com/downloads
4. install **Maven**. https://maven.apache.org/download.cgi


## Step 1 : Clone The Project
git clone https://github.com/lyrian12/SoccerSeason2026_2027
cd SoccerSeason
## step 2 : Start the Database With Docker
docker compose up -d  
> -d is for dettached so that it can allow the terminal to contiue execution while running the file.
## Step 3 : Run The Apllication
./mvnw spring-boot:run
## Step 4 : Open in browser

Go to https://localhost:9000


---

Need more help contact me on : lyrian12@gmail.com