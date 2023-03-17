local n = #KEYS
redis.call("del", "cache:INTERFACE:URL")
for i = 1, n do
    redis.call("hset", "cache:INTERFACE:URL", KEYS[i], ARGV[i])
end
return "1"
