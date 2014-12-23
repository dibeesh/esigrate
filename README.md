# esigrate

A Clojure application designed to Migrate/Copy Elasticsearch indices, based on [Nomad](https://github.com/codemomentum/nomad)

##Usage

* Dowload Esigrate tar file from releases.

* Ensure JAVA_HOME is set

* Extract tar file tar -xzf esigrate...tar

* cd esigrate...

* Execute Esigrate via bin/launcher.sh start

Esigrate starts at port 9090.

To see esigrate is running;

     curl -XGET http://localhost:9200


now you can start a migration like the following:

    curl -XPOST http://localhost:9090/migration -d '{
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


Check status of a migrate job:

    curl http://localhost:9090/migration/{id}

possible responses can be queued,running,stopped


To stop Esigrate;

bin/launcher.sh stop

## Behaviour

- wont index anything if src type is not successfully migrated to destination

## Development

    git clone https://github.com/codemomentum/es-migrate.git
    cd es-migrate
    lein run

##TODO

Migrate;

- percolators
- aliases

## License

Copyright 2015 www.searchly.com

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License. You may obtain a copy of the License in the LICENSE file, or at:

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and limitations under the License.


