# Eventer - the Comprehensive Even and Error Handling Framework


## Symptoms of a Problem

Did you ever wondered why your error handling in Java always sucks? Plenty of code written every day trying endlessly to try and catch the errors and then report it and push forward etc. Well it can be done better than that. In fact much better. And the method of doing so id described in this document.

### “Technical Debt“ is a Bad Analogy

Error handling is a part of the software developer’s life which is pushed into back of any work that is done. Proper reporting errors and any other noteworthy situations is usually left to be resolved at the end of the development cycle. Unfortunately, then when most of the code is done it is too late to make considerable changes in this matter because the code tends to get solidified as lava block. This makes any later changes almost impossible to be done.


### Examples of Bad Behavior

* Mixing the domains (layers)

* Abusing the exceptions

* Lack of centralized error handling

* Lack of consistency


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


## Rules

* If you have a non-recoverable situation, use the Emitter.throw() method.
* If you want to report that something happened, just emit an event and go on. Emitter.emit()
* Do not handle errors (exception) unless you need to recove any resources. In such situation try to use “finally” clause and let the other code to handle the rest.

## What it Should Be

* Rich event information
* Minimum code
  * (from dev perspective)
* Well separated concerns
* Well configurable
* Unified across all apps and layers
* Simple to implement, comprehend, and use

## What is in the Message

Now we can try to imagine what we want to achieve by sending the information back to the caller, or stored in the log message etc. These needs can be expressed by various types of properties that the message can carry:

* **ID**

  Unique identification of the message. This is always unique, no matter what is the message.
* Type of event ID

  Otherwise the `templateId`. This is the textual ID that represets the type of the message. For example such type may be “File not found”, always with the same textual message plus additional relevant context (like a file name in this scenario). 
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


## Design Principles

* **Simplicity of code**

  This is the first and umost important one, as the code is the place where the event are emitted. Therefore, the process of emitting the messages should be as little obstructive as it is possible. And it should also eliminate additional non-business logic code as possible.
* Completness of information
* Flexibility of use
* Reasonable performance

## Emitting Events

On the level of the language the events are emitted in two ways:

* by calling an emit() function, or
* by throwing the exception EventException().

## Configurability

* Definitions of messages can be kept as
  * enums
  * externalized (for example, into a .yml file)
* The granularity of the event information is configured in the .yml file (@Configuration class):
  * depending on the destination of the message:
    * logging
    * sending back to the client

## How this can work in Java

* One method is to just emit an event
* The other method is to throw an exception which then emits an event
* **Emit collection of events**

  Yet another is to collect events and then emit them (as a collection)

  This method is useful especially when the validation needs to be performed and it is very likely that multiple messages will be created and should be sent back in one call.

## How this can be handled by UI (caller)

Usually the caller is an UI application, likely a JavaScript based.

When the regular business call is made the 200 OK response is excpected.

When the error happens the 418 response is returned along with the body of the error message.

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


