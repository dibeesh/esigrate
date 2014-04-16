# nomad

A Clojure library designed to Migrate/Copy pre 1.0 ElasticSearch indexes to latest version

Post a json based structure similar to the following edn to start migration:

curl -x POST -d '

    {
        "id" :"001",
        "src": {
            "url":   "http://localhost:9200/",
            "index" :"index1"
        },
        "dest" :        {
            "url":   "http://newhost:9200/",
            "index": "index2"
        }
    }
'


Check status:

    curl http://localhost:8080/migration/{id}

possible responses can be queued,running,stopped

## Behaviour

- wont index anything if src type is not successfully migrated to destination

FIXME

##TODO
- percolator
- alias
## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
