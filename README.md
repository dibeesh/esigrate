# nomad

A Clojure library designed to Migrate/Copy ElasticSearch indexes



Post a json based structure similar to the following edn to start migration:

    {
        :migration-id "001"
        :src          {
            :url   "http://localhost:9200/"
            :index "index1"
        }
        :dest         {
            :url   "http://localhost:9200/"
            :index "index2"
        }
    }



## Usage

FIXME

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
