# async-http
> a simple project to do async http request


To run this project you will need use java 8 or higher, you can configure this in you pom.xml file.
``` 
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>
```

Only ```GET``` and ```POST``` methods are supported yet.


To know how use, look at the example below.

```
Lazuli lazuli = new Lazuli();
// creating headers
Map<String, String> headers = new HashMap<>();
headers.put("Accept-Charset", "application/x-www-form-urlencoded; charset=UTF-8");
headers.put("Accept-Language", "en-US,en;q=0.5");
headers.put("X-Riot-Token", "{token}");
// passing headers to be added to the connection
lazuli.setHeaders(headers);
// make a GET request
lazuli.get("https://br1.api.riotgames.com/lol/summoner/v3/summoners/by-name/3vilknight", response -> {
    System.out.println(response);
});
// this method forces the request to close even if doesn't completed.
// lazuli.cancel()
```

It's important to note, that you'll need pass a Consumer callback to do some work with the request response.
