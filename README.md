# nomad

A Clojure library designed to Migrate/Copy ElasticSearch indexes



Post a json based structure similar to the following edn to start migration:

curl -x POST -d '

    {
        "id" :"001",
        "src": {
            "url":   "http://localhost:9200/",
            "index" :"index1"
        },
        "dest" :        {
            "url":   "http://localhost:9200/",
            "index": "index2"
        }
    }
'


Check status:

    curl http://localhost:8080/migration/{id}

possible responses can be queued,running,stopped

## Behabiour

- wont index anything if src type is not successfully migrated to destination

## Todo
        types-exists

FIXME

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
