# Testing possible refactoring for making [HttpMethod](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpMethod.html) extensible

## Run

Simply run the tests

```gradle
./gradlew test
```


## Preamble

While trying to help [The Spring Framework](https://github.com/spring-projects/spring-framework)
I came across [this ticket](https://github.com/spring-projects/spring-framwork/issues/25109) which sounded very simple.

## Why not simply create a branch?
I found out early on that the HttpMethod Enum is used very intensively in many classes (a search in Intellij easily 
quickly returns more than 1333 usages).

I also established quite quickly that the refactoring proposals were very invasive in the code, meaning that a lot of 
changes are required. This seemed wrong.

I thought about various alternatives. I found it is quite easy to make an isolated comparison, each with a common pattern
of unit tests. 

At the end I present a small conclusion.

## Note
All refactoring were made with the intent of seamless update of the library, with as few as possible updates
of application code.

## Naive approach (package v2 in the code)

### Approach
1. Make **HttpMethod** an interface. 
2. Rename current HttpMethod to **HttpMethods** and extend the *newly refactored* **HttpMethod** interface.
3. Have all **HttpMethod.Get** point to **HttpMethods.GET** to not touch any existing references in code
4. Allow the creation of **NonStandardHttpMethods**. 
5. Make the *equals* method depend only on the String value of the **HttpMethod.name()** method, which returns a String.


### Problems making the refactoring cumbersome
#### Creation of EnumSets
E.g. [org.springframework.web.reactive.function.server.DefaultServerResponseBuilder.AbstractServerResponse](https://github.com/spring-projects/spring-framework/blob/90ccabd60bfe24249b3c4cbe43a25ffd0efa6eba/spring-webflux/src/main/java/org/springframework/web/reactive/function/server/DefaultServerResponseBuilder.java#L301)

```java
EnumSet.of(HttpMethod.GET, HttpMethod.HEAD)
```

#### Usage of switch statements on Enum

E.g. [org.springframework.web.reactive.function.server.ResourceHandlerFunction](https://github.com/spring-projects/spring-framework/blob/90ccabd60bfe24249b3c4cbe43a25ffd0efa6eba/spring-webmvc/src/main/java/org/springframework/web/servlet/function/ResourceHandlerFunction.java#L55)

```java
switch (method) {
    case GET:
        return EntityResponse.fromObject(this.resource).build();
    case HEAD:
        Resource headResource = new HeadMethodResource(this.resource);
        return EntityResponse.fromObject(headResource).build();
    case OPTIONS:
        return ServerResponse.ok()
                .allow(SUPPORTED_METHODS).build();
    }

```

#### Difficulties with the equals method

please see [this test](ro.mdumitrescu.httpmethod.v2.HttpMethodTest.java)

Note: test is disabled

## Better approach (package v3)

I tackled the problem of symmetric equals method.

The approach was to: 
1. Make an internal Enum StandardHttpMethods inside the interface HttpMethod
2. Create an inner class, NonStandardHttpMethod
3. Use the interface's static method **resolve**, which was present from the beginning, as a Factory Method to construct
the proper 

##### Pros to previous version

1. Equals method works properly
2. Easier to migrate for switch cases, by having application developers change static import from 
**HttpMethod.Get to HttpMethod.Standard.GET**, which is not so invasive

#### Still existing problems

1. One can not switch on **HttpMethod** anymore, because it is an interface, before [proper pattern matching](https://openjdk.java.net/jeps/8213076) 
is implemented in Java.

2. While one can now **switch** on **HttpMethod.Standard**, one must either

    a. cast **HttpMethod.resolve(String)** to **HttpMethod.Standar**, which is inelegant
    b. create a method like **resolveStandardMethod()** or **HttpMethod.Standard.resolve()** which makes
    the interface of HttpMethod quite cumbersome
    
    
## Conclusion

While it might sound appealing to make the HttpMethod to an interface, this brings a lot of breaking changes.   
The biggest are, imho, that one: 

1. Can not create EnumSets
2. Can not switch on interfaces


### Suggestion

Imho the easiest approach is to simply extend the current enum with the **CONNECT** method. 
The **CONNECT* HTTP Method is mentioned in [HTTP/1.1 RFC](https://tools.ietf.org/html/rfc7231#section-4.3.6) and
subsequently in [HTTP/2 RFC](https://tools.ietf.org/html/rfc7540#page-72)

One could consider if there are other non *trivial* HTTP methods in use (e.g. [LINK and UNLINK](https://tools.ietf.org/id/draft-snell-link-method-01.html))

This should not introduce a breaking change and should make the change quite easy. 


