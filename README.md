# nomad

A Clojure application designed to Migrate/Copy pre 1.0 ElasticSearch indexes to latest version

Post a json based structure similar to the following edn to start migration:

##Usage

    git clone https://github.com/codemomentum/nomad.git
    cd nomad
    lein run

you should now see a message like:

    INFO: Rest server started at port : 8080

now you can start a migration like the following:

    curl -XPOST http://localhost:8080/migration -d '{
            "id" :"001",
            "src": {
                "url":   "http://localhost:9200/",
                "index" :"index1"
            },
            "dest" :        {
                "url":   "http://newhost:9200/",
                "index": "index2"
            }
        }'


Check status:

    curl http://localhost:8080/migration/{id}

possible responses can be queued,running,stopped

## Behaviour

- wont index anything if src type is not successfully migrated to destination


##TODO
- percolator
- alias

~~parent/child~~

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
