# Find download URLs from OpenHub
I had to write this to find URLs for this project because we only wrote
our API to download the code from GitHub.

## Usage
You can compile with lein or use the compiled jar in the `binary` directory.

This will create/append `results.txt` and add downloaded XML to directory `down`.

Get usage:
```bash
$ java -jar binary/ohfinder-0.1.0-SNAPSHOT-standalone.jar 
find	find for github URLs on OpenHub
  -a, --apikey STRING       Your 64 character OpenHub API key
  -s, --start NUMBER    1   Starting range (see Blackboard)
  -q, --queries NUMBER  25  Number of queries/downloads (you get 1000/day)
```

Invoke:
```bash
$ java -jar binary/ohfinder-0.1.0-SNAPSHOT-standalone.jar find -a <APIKEY> -q 1
client: using API key: <0eb9e86d694602b9c3f32865b448eb3594ce45d9e9b961607a605b3b650fdfff>
client: starting at 1 for 1 queries
client: searching 1 - 2
client: checking ID: 1
client: url: http://subversion.apache.org/packages.html
client: complete for range: 1
```

## License
Copyright © 2016 Paul Landes

Apache License version 2.0

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
