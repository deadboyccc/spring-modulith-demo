#!/bin/zsh

for i in {1..10}
do
  echo "Sending request $i of 10..."
  sleep 1  # Wait for 1 second before sending the initial request
  
  http POST http://localhost:8080/api/content \
    title="Spring Modulith Post $i" \
    url="https://example.com/demo-$i" \
    type="BLOG_POST"
  
  # Wait 5 seconds before the next request (skip waiting after the last one)
  if [ $i -lt 10 ]; then
    echo "Waiting for 5 seconds..."
    sleep 5
  fi
done

echo "Finished sending all 10 requests!"