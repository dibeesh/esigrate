# Introduction to nomad

curl -XPUT http://api.searchbox.io/api-key/1ea4feb23a03d9db446e51cb37985df2/denemepc/author/1 -d '{"x":"y"}'
curl -XPUT http://api.searchbox.io/api-key/1ea4feb23a03d9db446e51cb37985df2/denemepc/author/2 -d '{"z":"k"}'
 
curl http://api.searchbox.io/api-key/1ea4feb23a03d9db446e51cb37985df2/denemepc/author/_search  | jsonpp
  
curl -XPOST http://api.searchbox.io/api-key/1ea4feb23a03d9db446e51cb37985df2/denemepc/author/_search -d '{ "query": { "has_child": { "type": "book", "query": { "filtered": { "query": { "match_all": {} } } } } } }' |jsonpp
  
   
curl -XPOST http://api.searchbox.io/api-key/1ea4feb23a03d9db446e51cb37985df2/denemepc/book/_mapping -d '{"book":{"_parent":{"type":"author"}}}'
curl -XPOST http://api.searchbox.io/api-key/1ea4feb23a03d9db446e51cb37985df2/denemepc/book/1\?parent\=1 -d '{"aa":"bb"}'
curl -XPOST http://api.searchbox.io/api-key/1ea4feb23a03d9db446e51cb37985df2/denemepc/book/2\?parent\=2 -d '{"aaa":"bbb"}'

