lighthttp
===

[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-lighthttp-green.svg?style=flat)](https://android-arsenal.com/details/1/2543)
[![Download](https://api.bintray.com/packages/satorufujiwara/maven/lighthttp/images/download.svg)](https://bintray.com/satorufujiwara/maven/lighthttp/_latestVersion)

Lightweitht HTTP client for Android.

# Features
* Simple HTTP client. 
* Depends on Android(HttpUrlConnection) only.
* Execute asynchronously or can use with RxJava.
* Convert request and response.

# Usage

Get a url.
```java
LightHttpClient httpClient = new LightHttpClient();
Request request = httpClient.newRequest()
    .url("https://api.github.com/users/satorufujiwara/repos")
    .get()
    .build();
Response<String> response = httpClient.newCall(request).execute();
String body = response.getBody();
```

## Converter

Convert response to java object.
```java
httpClient.setConverterProvider(new GsonConverterProvider());

Response<Repos> response = httpClient.newCall(request, Repos.class).execute();
Repos body = response.getBody();
```

Post json object.
```java
httpClient.setConverterProvider(new GsonConverterProvider());

Item obj = new Item();
Request request = httpClient.newRequest()
    .url(url)
    .post(obj, Item.class)
    .build();
```

## Executor

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
    compile 'jp.satorufujiwara:lighthttp:0.1.1'
}
```

## Converter
Gson
```groovy
compile 'jp.satorufujiwara:lighthttp-gson:0.1.1'
```

## Executor
Async
```groovy
compile 'jp.satorufujiwara:lighthttp-async:0.1.1'
```
RxJava
```groovy
compile 'jp.satorufujiwara:lighthttp-rx:0.1.1'
```

# Milestone

- [ ] Multipart request body.
- [ ] Jackson converter.
- [ ] Jsonic converter.
- [ ] Sample app.
- [ ] Testing.

# Developed By

Satoru Fujiwara (satorufujiwara)
* Twitter [satorufujiwara](https://twitter.com/satorufujiwara)
* holly.wist@gmail.com
 
Other Projects
* [recyclerview-binder](https://github.com/satorufujiwara/recyclerview-binder)
 * Android Library for RecyclerView to manage order of items and multiple view types. 
* [material-scrolling](https://github.com/satorufujiwara/material-scrolling)
 * Android library for material scrolling techniques.

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
