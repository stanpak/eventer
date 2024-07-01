# Event Capture Across System Boundaries

One of the interesting problems is how to capture the events across multiple systems or the system components. When the system components are interacting the resulting events form a tree-like structure of events. 

In order to bind all of the events together to form that tree of events. There is a need for? a An identifier that would be used to Identify the span. Of the whole interaction. This identifier would need to be passed To any Interacting parties Subsequent in the chain. 

At each call to the other system the span identifier would need to be passed and the child span would be formed.

Let's consider this sequence below. It shows one call to the API, which then calls another LDAP system to authenticate the user and check its access rights. After providing the results from the LDAP. The functionality continues. Some calculations are done. And then it makes yet another call to external system. That system does some calculations and returns the results back to the caller. Then the caller combines all the results and returns them back to the calling system. .

```
msg1: API endpoint called
msg2: About to call LDAP to authorise the caller
    msg2.1: LDAP called
    msg2.2: User found: ...
msg3: Doing something noteworthy
msg4: Calling external system
    msg4.1: Called ext system function
    msg4.2: Doing some calculations
    msg4.3: Making call to yet another external system
    msg4.3.1: Doing the calculations and returning some results
    msg4.4: Combining the results and returning them to the client
msg5: Got the results from the ext system:...
msg6: Returning the results to the caller of the API
```

In the above sequence, we can see that the events form a tree structure of events composed of three levels in the tree. The levels correspond to the call sequence.

Each of the levels of of the tree represents the call that was made to entirely separate system.

In order to preserve all the events that happen in all that transaction. It is important to have one common place with that information can be stored. When all the sequence within that transaction is finished. The information about that the tree of events. Can be stored somewhere permanently. For example, it can be a log. 

Since that information is quite ephemeral, it is suggested that that system should be in form of a cache. A distributed cache system like Redis.

```javascript
{
    spanId: "...",
    events:[
        // First event message
        {
            id: "..."
            templateId: "...",
            message: "...",
            ...
        },
        // Second event message
        {
            id: "..."
            templateId: "...",
            message: "...",
            ...
        },

        // Information from external system
        {
            // child span
            spanId: "...",
            events:[
                //...
            ]
        },

        // Yet anothe event message...
        {
            id: "..."
            templateId: "...",
            message: "...",
            ...
        },
    ]
}
```
## How to span information travels across the system boundaries?

It is assumed that all of the system components, the services they all show the event reporting service. Event reporting service is a system that was already mentioned earlier and it may use distributed cache that stores all the history of the events and all the branches of that event tree.  

In order for the clients system. to know what is the. what is the context of the call The parent system that is calling the child system needs to provide a span ID. This span ID can be used by the child system to properly indicate the context of any sequence of events that are produced there.


