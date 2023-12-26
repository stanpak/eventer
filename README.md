# Eventer - the Comprehensive Event and Error Handling Framework


## Symptoms of a Problem

Did you ever wondered why your error handling in Java always sucks? Plenty of code written every day trying endlessly to try and catch the errors and then report it and push forward etc. Well it can be done better than that. In fact much better. And the method of doing so id described in this document.

### “Technical Debt“ is a Bad Analogy

Error handling is a part of the software developer’s life which is pushed into back of any work that is done. Proper reporting errors and any other noteworthy situations is usually left to be resolved at the end of the development cycle. Unfortunately, then when most of the code is done it is too late to make considerable changes in this matter because the code tends to get solidified as lava block. This makes any later changes almost impossible to be done.


### Examples of Bad Behavior

* **Mixing the business domains or layers**

  It is a frequent mistake when the REST calls use the HTTP status to report an error, when there something happened in the business logic that prevented the operation from succeeding. For example when something requested by the UI was not found in the database, the back end returns the response 404 Not Found. This is a mistaken use as this status is used by HTTP layer to report that the end point is not recognized. Use of HTTP statuses should be discouraged as it masks the legitimate HTTP problems with business logic related functionality.   
  
  Business lgic error or state reporting should be clearly separated from the HTTP error/status reporting.


* **Abusing the exception mechanism**

  Another frequent misuse of available error handling mechanisms is reporting trivial (and expected) troubles by the business logic by throwing Java exceptions. For example when certain requested object is not found in the result, instead of returning `null` or empty results or array, the programer throws an exception. It is a mistake as not finding something is legitimate and expected business behavior and should be handled as such. The exceptions should be thrown in situations when something happens outside of the business logic context that is unexpected (file not found, connection broken etc.) and it is a unrecoverable situation that prevents the business operation from succeeding.


* **Lack of centralized error handling**

  Handling of errors should not happen at every controller's handling function. This method has various drawbacks, as it creates tons of inconsistent code, where the errors are handled in different ways from function to function. This causes inconsistency, requires additional effort from the programmer and results in waste of valuable time, while introducing human error potential.


* **Lack of consistency**

  It was mentioned already, when not standardized, each of the programmer will be implementing their own ways of handling errors according to their knowledge (which vary). Instead we need a predictable way of handling errors that will also free the developer to work on the business logic and not on error handling boilerplate code that obscures the most important part of the code. 


### Other Transgressions

* **No standard of a message**

  Each layer (participant) uses its own format of keeping information about the event. This creates inconsistency o reporting about the events. Joining the events in a consistent lineage is also hard as not always the timing information is available.


* **No standards for handling events**

  Each layer (participant) uses its own specific handling procedures (i.e. some handle exception some not so no information is preserved). Most prevalent way is passing that information via exceptions body, but logging the information into the database or in file system is also a popular alternative.


* **Separation of event handling from logging**

  Mechanism of logging and handling of exceptions and other event types is separate from each other.

  That requires the developer to write proper code for handling a message and to decide if it should be logged or not. Logging is just a particular way (of many) of handling the events.


* **Little support for developer**

  In each case the decision of loggin and event is in developers’ hands and is laborious, mechanistic and error prone process (which in effect causes that logging all details of exception is neglected in many cases).


* **No context infromation is preserved**

  There is no standard way of keeping context describing circumstances of an event (exception) which is essential to properly diagnose a problem at question.


* **Messages are hardcoded**

  Practically all messages that are generated in the code are hardcoded by the developers. That provides no way of improving the content of the message without release of the system.


* **Lack of quality management process**

  There is no place in the development process where the developer is obliged to (besides of commenting and describing the code properly) provide extensive error/event handling information that can be essential for diagnosing problems in complex distributed systems.


* **Lack of coordination of flow in distributed environment**

  Systems are distributed and there is no easy way to corroborate all events in order to analyse the properly circumstances of an event and identify causes of the problem.


* **No proper mechanism of managing the message granularity**

  There is no need to always pass ALL information about the history of transaction to the originating participant. Thus about what information is passed to the originator is decided not by it but by the underlying system which does not know the needs of the originator. Recipient of the event information should decide about granularity of the information about the event it should get for analysis. •No convenient storage of event history There is no convenient storage of the event information that would be available for all layers/participants.


* **No standard way of analysing complex information about the problem**

  If an event happens there is no one way of analysing that information from one point of the complex distributed system.


## The Guiding Principles and a Vision

Below is a list of the things that we could imagine the ideal event handling system should provide, namely:

* **Rich event information**

  We would like to know what happened, when, where and in what circumstances, caused by what and what to do with this etc. In other words: everything that is possible to collect about a given event.


* **Minimum coding effort**

  From the developer perspective there should as little coding effort as required and no more. There should be little margin for error as well.


* **Well separated concerns**

  Any design of a framework should observe the rule of separation of concerns so each class has well defined properties and functions that do not interfere or intersect other classes. This allows the modularity, increases the ease of understanding and working with the framework, reducing human error.


* **Well configurable**

  Anything that can be configurable should be made configurable.


* **Unified across all apps and layers**

  Possibly compatible between various languages beyond Java - for example JavaScript. So the messages can be easily consimed by the UI.


* **Simple to implement, comprehend, and use**

  Well, that is self-explanatory.

### Simplicity of Use
This is the first and most important one, as the code is the place where the event are emitted. Therefore, the process of emitting the messages should be as little obstructive as it is possible. And it should also eliminate additional non-business logic code as possible.

On the level of the language the events are emitted in two ways:

* by calling an emit() function, or
* by throwing the exception EventException().

### Flexibility through Configuration

One of the method of achieving the goal of flexibility is to allow high level of confuguring the way in how the exevits are propagated and what the messages contain, etc. 

#### Message Template Definitions

The Ddefinitions of messages can be kept in following forms:
  * **Enums (or rather Java constants)**

    This method is just a way of hard-coding the message templates into the code. 
    It can be useful for systems that can be recompiled frequently after changing the text of messages.

    Since in Java the it is impossible to inherit the enum structure we need to use regular classes. Below is an example of such class definition:

    ```java
    public class SampleErrorMessage extends EventMessageTemplate {
        public static SampleErrorMessage Message00 = new SampleErrorMessage("msg00","Error has happened!");
    
        public SampleErrorMessage(String templateId, String message) {
            super(templateId, message);
        }
    }
    ```

    When used in code it can be simply invoked in `Emitted.emit()` method:

    ```java
    Emitter.emit(SampleErrorMessage.Message00);
    ```

  * **Externalized (for example, into a .yml file)**

    If the messages need to be kept in the outside file that can be loaded in runtime and any modifications do not require the recompilation of the code - than use of externalized message templates goes handy.

    One of the method is to keep them in structured fashion in YML files.

    Below is the example of such a message template definition:

    ```yaml
    error-handling:
      templates:
        -
          templateId: "msg00"
          message: "Terrible thing just happened, context: %s"
        -
          templateId: "msg02"
          message: "Terrible thing just happened, context: %s"
    
    ```
  * **Database**

    If even greater level of flexibility of modifying the text of message templates is required, these definitions can be kept in the database.

#### Event Message Granularity

The granularity of the event information is configured in the .yml file (@Configuration class):
  * depending on the destination of the message:
    * logging
    * sending back to the client
    * displaying in the console

    Below is an example of such definition:

    ```yaml
    error-handling:
    
      # Options of the message that is returned to the REST caller
      restResponse:
        templateId: true
        timing: true
        message: true
        causes: true
        context: true
        howToFix: true
        exceptionInfo: true
        stackTrace: false
        location: true
    
    ```
    
* **Reasonable performance**

## What is in the Message

Now we can try to imagine what we want to achieve by sending the information back to the caller, or stored in the log message etc. These needs can be expressed by various types of properties that the message can carry:

* **Event ID**

  Unique identification of the event in the message. This value is always unique, no matter what is the message (UUID).


* **Type of events**

  Various events can be emitted over and over again. For example "File not found" error event may be happening frequently, just with different context (file name). Therefore it may make sense to keep the text of the message externalized in form of a template that later at the message emission it will be found and used to form the message.

  Type of events is otherwise called as a message template and identified by a string (unique for the application where it is used.) - a `templateId`.
  For example such type may be “File not found”, always with the same textual message plus additional relevant context (like a file name in this scenario).


* **Category of the event**

  Optional categorization of the message. It is used to filter the messages. It may signify the purpose of the message or the severity of the event, etc.


* **Textual message**

  Textual form of a message with placeholders where the context can be injected where needed.


* **When it happened**

  Information about temporal context of the event. For example, the timestamp when the message was emitted. It can hold more than that and multiple of timestamps from various stages of event message processing can be recorded.


* **Where it happened** (process, thread, class, function, line etc.)

  This information is very important as it shows where the emission of the message was initiated. It is generated automatically by the framework.


* **Stack trace**

  Additional more robust information on what was the sequence of calls that let do the moment when the event was emitted.


* **Additional context** (state of variables)

  Very important information. In order to find the true cause of the error that resulted with the event message being emitted, the information in the message must carry the contextual information that can be vital in finding the cause of the error. These can be values of variables for example, selected by the developer in anticipation that they can be useful at the later moment.


* **Possible causes**

  Here we can carry some speculations on what could cause this problem more specifically.


* **Hints: How to fix, what to do with this**

  In addition to the information on possible cause, we can provide the advice to the user on how to deal with this issue, work around it, where to report it, to whom to escalate, etc.


* **Listing multiple messages**

  Sometimes the event message can carry multiple aggregated messages. This makes sense if the information in these messages are co-related, as in case of for example of validation messages.


* **Embedding the message trail** (if needed)

  Sometimes if the event message was caused by another event message, then we can embed it and carry it on with the subsequent message for additional context.

Here is the example of a full message in form of the JSON:

```json
{
    "id": "b4e36462-f27a-4fd5-9b13-b5b87bd47406",
    "templateId": "msg01",
    "message": "Terrible thing just happened (no context)",
    "emittedAt": "2023-12-26T17:53:02.023+00:00",
    "location": {
        "hostname": "staszek-Latitude-5511",
        "processId": "358008@staszek-Latitude-5511",
        "file": "Thread.java",
        "function": "getStackTrace",
        "line": 2450,
        "class": "java.lang.Thread",
        "native": false
    },
    "stackTrace": [
        "java.base/java.lang.Thread.getStackTrace(Thread.java:2450)",
        "com.tribium.eventer.core.EventCapturer.capture(EventCapturer.java:48)",
        "com.tribium.eventer.core.Emitter._emit(Emitter.java:22)",
        "com.tribium.eventer.core.Emitter.emitThrow(Emitter.java:43)",
        "...",
        "org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)",
        "java.base/java.lang.Thread.run(Thread.java:1583)"
    ],
    "cause": "<Here there is a chain of exceptions when this messgae is thrown as a result of other exceptions.>",
    "possibleCauses": ["<List of description of various possible causes - intended to be displayed to the user>"],
    "howToFix": ["<List of tips how to resolve this problem, whoe should be notified, how to escalate, etc.>"],
    "ExceptionClassName": "<Name of the exception if this event was triggered by throwing an exception>",
    "messages": ["<If this message is in fact an aggregation of multiple messages, here they can be listed, along with additional context.>"]
}
```

## How is it Used in Java Code

The most common scenario is the situation when the developer wants to add some code that will signal to the outside world (log, console etc.) that something has happened. This can be done by inserting just one method call to just emit an event:

```java
Emitter.amit("msg00");
```

Above instruction is an example of minimum call from the Java code. The parameter is a ID of the message template. The deferrence to the templates allow externalization of the messages and avoiding hardcoding them in the application’s code.

Sometimes the developer needs to add some additional context, for example values of some crucial variables that should help to narrow down the source of a problem. 
In such scenario the emission of the event message can have additioal parameters, which are part of the context of the message. These context parameters can be not only attached to the message but also added to a message itself:

```javascript
Emitter.emit("msg01",var1, var2);
```

where the message of a template can look like this: `”An error happened, var1: %s, var2: %s”`. You get the idea.

### When the error occured and needs to be reported
If the error happened and we want to emit an error and throw the exception we can do it in 2 ways:
  * **We can throw the Java exception.** 
    
    If used in Spring Boot application, this should be handled and converted to a message event prior sending it back to the caller.

    ```java
    throw new RuntimeException("Error just happened!");
    ```
    This is very generic and simple method of reporting errors, however it has a drawback that it does not rely on more advanced mechanism of error handling. This is just the Java exception handled by our mechanism to some extent but without templating and other features.
    

  * **Throw the EventException**
    
    This type of exception is already integrated with our framework and when thrown, it will use the message templating mechanism. When handled, the full error information will be reported back to the user.
    ```java
    throw new EventException("msg00", var1, var2);
    ```
    This method still relies on the code which handles these exception to emit a message.
    

  * **Using emitThrow()**
    
    If we want to not only throw an exeption but emit w message at this point of code we can use `Emitter.emitThrow()` method. Behavior is as expected but this time we can not only throw an exception but also use message templating mechanism.
    This is the preferred method of reporting the error by the application's code.

### **Emiting collection of events in one message**

  Yet another is to collect events and then emit them (as a collection)

  This method is useful especially when the validation needs to be performed and it is very likely that multiple messages will be created and should be sent back in one call.

  Conside following example:

  ```javascript
  EventMessage m = new EventMessage();
  m.addMessage("msgTemplateId1",...);
  m.addMessage("msgTemplateId2",...);
  Emitter.emit(m);
  ```

  It embeds two messages into specific final message and then this one is emitted (or thrown back to the caller client if needed).

  This mechanism is especially useful for validation purposes as we can return a list of problems with our data.

## How to Handle Events by the UI (caller)

Usually the caller is an UI application, likely a JavaScript based.

When the regular business call is made the 200 OK response is excpected.

When the error happens the 418 response is returned along with the body of the error message.

This HTTP status was chosen specifically so it does not highjacks any other HTTPS status code for reporting the business logic errors and thus it does not obscure any other valid errors that can be reported by the HTTP protocol layer. 

The sample code below should be easy to apply in any JavaScript application that responds to the REST calls returning from the back end.

```javascript
try{
  let res = await http.get("http://myapp/myednpoint");
  // normal expected business behavior 
}catch(err){
  // an error occured - 418 status returned
  // err contains rich information on the error message sent from the BE.
  displayErrorInfo(err);
}
```

As this is the only way of sending back the error information the UI developers can create one standardized way of handling these errors and decide which parts of the message and how they are presented to the End User.

## Simple Rules on How to Code
Here are some helpful rules on how to use this framework when working on your code:
* **Rule #1**: If you have a non-recoverable situation, use the `Emitter.emitThrow()` method.
* **Rule #2**: If you want to report that something happened, but it not a situation that prevents your operation to be finished, just emit an event (`Emitter.emit()`) and go on.
* **Rule #3**: Do not handle errors (exception) unless you need to recover any resources. In such situation try to use “finally” clause and let the other code to handle the rest.
