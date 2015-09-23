lighthttp
===

*This library is experimental. API and all codes will be changed without notice*

Lightweitht HTTP client for Android.

# Features

# Usage

Get a url.
```java
LightHttpClient httpClient = new LightHttpClient();
Request request = new Request.Builder()
    .url("https://api.github.com/users/satorufujiwara/repos")
    .get()
    .build();
Response<String> response = httpClient.newCall(request).execute();
String body = response.getBody();
```

Convert response to java object.
```java
httpClient.setConverterProvider(new GsonConverterProvider());

Response<Repos> response = httpClient.newCall(request, Repos.class).execute();
Repos body = response.getBody();
```

Execute asynchronously.
```java
httpClient.newCall(request, Repos.class)
        .executeOn(AsyncExecutor.<Repos>provide())
        .executeAsync(new AsyncCallback<Repos>() {
            @Override
            public void onResult(Response<Repos> response, Throwable e) {
                
            }
        });
```
Use with [RxJava](https://github.com/ReactiveX/RxJava).
```java
httpClient.newCall(request, Repos.class)
            .executeOn(RxExecutor.<Repos>provide())
            .asObservable()
            .subscribeOn(Schedulers.io())
            .subscribe();
```


# Gradle

```groovy
dependencies {
    compile 'jp.satorufujiwara:lighthttp:0.0.1'
}
```

## Converter
Gson
```groovy
compile 'jp.satorufujiwara:lighthttp-gson:0.0.1'
```

## Executor
Async
```groovy
compile 'jp.satorufujiwara:lighthttp-async:0.0.1'
```
RxJava
```groovy
compile 'jp.satorufujiwara:lighthttp-rx:0.0.1'
```


License
-------
    Copyright 2015 Satoru Fujiwara

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
