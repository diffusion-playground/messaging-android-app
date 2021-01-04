[![Video Tutorial](https://github.com/pushtechnology/tutorials/blob/master/messaging/video.png)](https://www.pushtechnology.com/blog/how-to-build-a-real-time-messaging-app-using-diffusion/)
# Real-Time Messaging

Introduction to Diffusion Real-Time Messaging through a simple application using [Diffusion](https://www.pushtechnology.com/product-overview) Cloud.

A set of simple projects, illustrating production and consumption of messages to and from a Diffusion Cloud instance.

These JavaScript code examples enable users to interact in real-time across various rooms or topics. You can also use other programming languages from our [SDKs](https://docs.pushtechnology.com/#sdks), including iOS, [Android](https://github.com/pushtechnology/tutorials/tree/master/messaging/android/diffusion-msg-app-L1), C, .NET, and more. 

# Lesson 1: Publish and Subscribe to Topics
[diffusion-msg-app-L1](https://github.com/pushtechnology/tutorials/tree/master/messaging/diffusion-msg-app-L1) introduces the concept of Publish and Subscribe to ['Topics' (data structures)](https://docs.pushtechnology.com/docs/6.5.1/manual/html/introduction/overview/topics_data.html). In Diffusion, data is stored and distributed through Topics.

# Lesson 2: Time Series Topics
[diffusion-msg-app-L2](https://github.com/pushtechnology/tutorials/tree/master/messaging/diffusion-msg-app-L2) introduces the concept of [Time Series](https://docs.pushtechnology.com/docs/6.5.1/manual/html/designguide/data/topics/timeseries_topics.html) topic to hold a sequence of events. Time series topics are useful for collaborative applications such as chat rooms. Multiple users can concurrently update a time series topic.

# Lesson 3: Security
[diffusion-msg-app-L3](https://github.com/pushtechnology/tutorials/tree/master/messaging/diffusion-msg-app-L3) introduces the concept of [Security](https://docs.pushtechnology.com/docs/6.5.1/manual/html/designguide/security/c_security.html) by requiring sessions to [authenticate](https://docs.pushtechnology.com/docs/6.5.1/manual/html/designguide/security/useraccess/atn_model.html) and use [role-based](https://docs.pushtechnology.com/docs/6.5.1/manual/html/designguide/security/roles_permissions.html) authorization to define the actions that a client can perform.

# The code in action
[![Video Tutorial](https://github.com/pushtechnology/tutorials/blob/master/messaging/diffusion-msg-app-L1/images/code-example.png)](https://youtu.be/tTx8q4oPx7E?t=336)

# Pre-requisites

*  Download our code examples or clone them to your local environment:
```
 git clone https://github.com/pushtechnology/tutorials/
```
* A Diffusion service (Cloud or On-Premise), version 6.5.0 or greater. Create a service [here](https://management.ad.diffusion.cloud/).
* Follow our [Quick Start Guide](https://docs.pushtechnology.com/quickstart/#diffusion-cloud-quick-start) and get your service up in a minute!

# Setup

Make sure to add Diffusion library to your code. For JavaScript, we have added the following line in our `public/chat.html`:
```
<script src='https://download.pushtechnology.com/clients/6.5.1/js/diffusion-6.5.1.js'></script>
```
Update `public/js/app.js` to the hostname of your Diffusion Cloud service, which you can find in your service dashboard.
You can also leave the default values and connect to our sandbox service:
```
* host: 'diffusionchatapp.eu.diffusion.cloud'
* user: 'user'
* password: 'password'
```
