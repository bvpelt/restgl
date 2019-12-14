# Restgl

```bash
bvpelt@pluto:~$ sudo /bin/bash
[sudo] password for bvpelt: 
root@pluto:/home/bvpelt# su postgres
postgres@pluto:/home/bvpelt$ createdb restgl
postgres@pluto:/home/bvpelt$ psql restgl
psql (12.1 (Ubuntu 12.1-1.pgdg19.10+1), server 11.6 (Ubuntu 11.6-1.pgdg19.10+1))
Type "help" for help.

restgl=# GRANT ALL PRIVILEGES ON DATABASE graphql TO testuser WITH GRANT OPTION;
GRANT
```

## Rest calls
### Adding data
```bash
$ curl -i -X POST -H "Content-Type:application/json" -d  '{"type":"Hibride","modelCode":"Yaris","brandName":"Toyota","launchDate":"2012-12-24"}' http://localhost:8080/vehicles 
HTTP/1.1 201 
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
Location: http://localhost:8080/vehicles/1
Content-Type: application/hal+json
Transfer-Encoding: chunked
Date: Sat, 14 Dec 2019 17:28:40 GMT

{
  "type" : "Hibride",
  "modelCode" : "Yaris",
  "brandName" : "Toyota",
  "launchDate" : "2012-12-24",
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/vehicles/1"
    },
    "vehicle" : {
      "href" : "http://localhost:8080/vehicles/1"
    }
  }
}
```
By posting the same data over and over one gets new vehicle id's but the contents of the data is
identical. There is no check to determine if the data already exists!
### Changing data
```bash
$ curl -i -X PUT -H "Content-Type:application/json" -d  '{"type":"Hibride","modelCode":"Avensus","brandName":"Toyota","launchDate":"2012-12-24"}' http://localhost:8080/vehicles/1 
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
Location: http://localhost:8080/vehicles/1
Content-Type: application/hal+json
Transfer-Encoding: chunked
Date: Sat, 14 Dec 2019 17:31:15 GMT

{
  "type" : "Hibride",
  "modelCode" : "Avensus",
  "brandName" : "Toyota",
  "launchDate" : "2012-12-24",
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/vehicles/1"
    },
    "vehicle" : {
      "href" : "http://localhost:8080/vehicles/1"
    }
  }
}
```
### Get data
```bash
$ curl -i -X GET http://localhost:8080/vehicles/1
HTTP/1.1 200 
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
Content-Type: application/hal+json
Transfer-Encoding: chunked
Date: Sat, 14 Dec 2019 17:33:04 GMT

{
  "type" : "Hibride",
  "modelCode" : "Avensus",
  "brandName" : "Toyota",
  "launchDate" : "2012-12-24",
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/vehicles/1"
    },
    "vehicle" : {
      "href" : "http://localhost:8080/vehicles/1"
    }
  }
} 
```
### Paging and sorting
```bash
$ curl -i -H "Accept: application/json" "http://localhost:8080/vehicles?page=0&size=2&sort=id"
HTTP/1.1 200 
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sat, 14 Dec 2019 17:39:29 GMT

{
  "_embedded" : {
    "vehicles" : [ {
      "type" : "Hibride",
      "modelCode" : "Avensus",
      "brandName" : "Toyota",
      "launchDate" : "2012-12-24",
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/vehicles/1"
        },
        "vehicle" : {
          "href" : "http://localhost:8080/vehicles/1"
        }
      }
    }, {
      "type" : "Hibride",
      "modelCode" : "Yaris",
      "brandName" : "Toyota",
      "launchDate" : "2012-12-24",
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/vehicles/2"
        },
        "vehicle" : {
          "href" : "http://localhost:8080/vehicles/2"
        }
      }
    } ]
  },
  "_links" : {
    "first" : {
      "href" : "http://localhost:8080/vehicles?page=0&size=2&sort=id,asc"
    },
    "self" : {
      "href" : "http://localhost:8080/vehicles"
    },
    "next" : {
      "href" : "http://localhost:8080/vehicles?page=1&size=2&sort=id,asc"
    },
    "last" : {
      "href" : "http://localhost:8080/vehicles?page=12&size=2&sort=id,asc"
    },
    "profile" : {
      "href" : "http://localhost:8080/profile/vehicles"
    }
  },
  "page" : {
    "size" : 2,
    "totalElements" : 25,
    "totalPages" : 13,
    "number" : 0
  }
}
```

### Find possible searches
```bash
$ curl http://localhost:8080/vehicles/search
{
  "_links" : {
    "findByModelCode" : {
      "href" : "http://localhost:8080/vehicles/search/findByModelCode{?modelCode}",
      "templated" : true
    },
    "self" : {
      "href" : "http://localhost:8080/vehicles/search"
    }
  }
}
```
### Use userdefined search
```bash
$ curl -i GET -H "Accept: application/json" "http://localhost:8080/vehicles/search/findByModelCode?modelCode=Avensus"
curl: (6) Could not resolve host: GET
HTTP/1.1 200 
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sat, 14 Dec 2019 17:59:11 GMT

{
  "_embedded" : {
    "vehicles" : [ {
      "type" : "Hibride",
      "modelCode" : "Avensus",
      "brandName" : "Toyota",
      "launchDate" : "2012-12-24",
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/vehicles/1"
        },
        "vehicle" : {
          "href" : "http://localhost:8080/vehicles/1"
        }
      }
    } ]
  },
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/vehicles/search/findByModelCode?modelCode=Avensus"
    }
  }
}
```
## Postgresql Monitoring
- see https://www.postgresql.eu/events/pgconfeu2018/schedule/session/2166/
- see https://github.com/wrouesnel/postgres_exporter