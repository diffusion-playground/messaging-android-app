# diffusion-msg-app-L1

Introduction to Diffusion Real-Time Messaging through a simple application using [Diffusion](https://www.pushtechnology.com/product-overview) Cloud.

Users can interact in realtime across various topic rooms.

Simple projects, illustrating production and consumption of messages to and from a Diffusion Cloud instance.

# Lesson 1: Publish and Subscribe

**diffusion-msg-app-L1** introduces the concept of Publish and Subscribe to ['Topics' (data structures)](https://docs.pushtechnology.com/docs/6.5.1/manual/html/introduction/overview/topics_data.html).

In Diffusion, data is stored and distributed through Topics. Session can subscribe to a topic to receive notifications when the topic value changes and can also update the value. When a topic is updated, all its subscribers are notified of the new value. Diffusion takes care of efficiently broadcasting value changes, even if there are hundred of thousands of subscribers.

# APIs used in this application

## **Step 1: Connect to Diffusion**
### [SessionFactory](https://docs.pushtechnology.com/docs/6.6.0-preview.1/android/com/pushtechnology/diffusion/client/session/SessionFactory.html) > [*create your host*](https://management.ad.diffusion.cloud/)
Provided an Activity where we received userName, password, and difussionService (its URL) as part of the intent:
```java
/* Session must be set in the Activity's onCreate function */
protected void onCreate(Bundle savedInstanceState) {
	/* sessionHandler is a private class where we handle the session, described in the NEXT STEP */
    private SessionHandler sessionHandler = null;
	...
	
	Diffusion.sessions() //The sessions factory implementation
	        .principal(this.userName) //The username to validate to the service
	        .password(this.password) // The password
	        // And we pass the sessionHandler instance to the session.
	        .open("ws://" + this.diffusionService, this.sessionHandler); //This is the funtion that actually opens a connection to the server
	        /* Use your Diffusion service or connect to our sandbox "diffusionchatapp.eu.diffusion.cloud" */
    ...
}
```
## **Step 2: Create a Topic**
### [session.topics.addTopic](https://docs.pushtechnology.com/docs/6.6.0-preview.1/android/com/pushtechnology/diffusion/client/features/control/topics/TopicControl.html#addTopic-java.lang.String-com.pushtechnology.diffusion.client.topics.details.TopicType-)
```java
/* This is the SessionHandler class to handle the diffusion service session */
private class SessionHandler implements SessionFactory.OpenCallback {
	/**
         * This function is called when the session is Opened
         * In this function we create the topic, and subscribe to it.
         * Subscribing to it will allow us to listen to everything streamed into the Topic's channel
         * @param session This is the session we created in the Activity's constructor
         */
        @Override
        public void onOpened(Session session) {
        	this.session = session;

            // Here is where we add the topic to the session
            this.session.feature(TopicControl.class).addTopic(
                    this.chatRoomName,
                    TopicType.JSON
            );

            // Attach a Stream to listen for updates (Step 3)
            ...
        }
}
```
### Go to: [Diffusion Cloud > Manage Service > Console > Topics](https://management.ad.diffusion.cloud/#!/login)
We are seeting up `chatRoomName` with the topic path: `Chat/Default Room`
![](https://github.com/pushtechnology/tutorials/blob/master/messaging/diffusion-msg-app-L1/images/topics.png)

## **Step 3: Create a Topic Listener**
In the **onOpened** function of the SessionHandlerClass, add the Stream we want to listen to
### [session.addStream](https://docs.pushtechnology.com/docs/6.6.0-preview.1/android/com/pushtechnology/diffusion/client/features/Topics.html#addStream-java.lang.String-java.lang.Class-com.pushtechnology.diffusion.client.features.Topics.ValueStream-)
```java
// Attach a Stream to listen for updates
this.session.feature(Topics.class).addStream(this.chatRoomName, JSON.class, new Topics.ValueStream.Default<JSON>() {                
    /**
     * This function gets called when new data is read from the Topic's channel
     * @param topicPath
     * @param topicSpec
     * @param oldValue
     * @param newValue
     */
    @Override
    public void onValue(String topicPath, TopicSpecification topicSpec, JSON oldValue, JSON newValue) {
        System.out.println("New value for" + topicPath + ": " + newValue.toJsonString());
        try {
            receiveMessage(newValue);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
});	
```
## **Step 4: Subscribe to a Topic**
Also from inside the **onOpened** function and **after** we started listening to the Stream, we subscribe to the topic. **chatRoomName**, was previously fed to the Activity via intent:
### [session.subscribe](https://docs.pushtechnology.com/docs/6.6.0-preview.1/android/com/pushtechnology/diffusion/client/features/control/topics/SubscriptionControl.html)
```java
// And we finally subscribe to the topic
this.session.feature(Topics.class).subscribe(this.chatRoomName);
```
## **Step 5: Update a Topic**
### [session.topicUpdate.set](https://docs.pushtechnology.com/docs/6.6.0-preview.1/android/com/pushtechnology/diffusion/client/features/TopicUpdate.html)
```java
// Convert it to a JSON value
final JSON value = jsonDataType.fromJsonString(message.toString());

// Now we send the message
final CompletableFuture<?> result = this.sessionHandler.
    getSession().feature(TopicUpdate.class).
    set(this.sessionHandler.getSessionTopicName(), JSON.class, value);
```
# The code in action
[![Video Tutorial](https://github.com/sebas-pushtechnology/diffusion-tutorials/blob/main/messaging/android/images/tutorial_l1.png)](https://youtu.be/tTx8q4oPx7E?t=336)

# Pre-requisites

*  Download our code examples or clone them to your local environment:
```
 git clone https://github.com/pushtechnology/tutorials/
```
* A Diffusion service (Cloud or On-Premise), version 6.5.0 or greater. Create a service [here](https://management.ad.diffusion.cloud/).
* Follow our [Quick Start Guide](https://docs.pushtechnology.com/quickstart/#diffusion-cloud-quick-start) and get your service up in a minute!

# Setup

Download the Diffusion [Android client](https://download.pushtechnology.com/clients/6.6.0-preview.1/android/diffusion-android-6.6.0-preview.1.jar).

In Android Studio, create a new project using API Level 19 or later, then copy the diffusion-android-6.6.0-preview.1.jar file to the app/libs directory in your project.

Find the library by expanding the app/libs folder in the left-hand panel (the Project Tool Window). If the libs folder is not shown in the left-hand panel, use the pull-down menu at the top of the panel to select the Project view.

Right-click on the jar, select **Add as Library...** then accept the default library settings.

Right-click on the libs folder in the Project Tool Window, then select Add as Library. If the libs folder is not shown in the left-hand panel, use the pull-down menu at the top of the panel to select Project view.

Add the following code to the build.gradle file within your project:

```java
compileOptions {
  sourceCompatibility JavaVersion.VERSION_1_8
  targetCompatibility JavaVersion.VERSION_1_8
 }
```

In your project's `app/src/main/AndroidManifest.xml`, set the INTERNET permission.

```java
<uses-permission android:name="android.permission.INTERNET"/>;
```

All that's left to do, is to add you own credentials to the Difussion.session() command, as done in the ChatActivity.java file.

You can also leave the default values and connect to our sandbox service:
* host: host ("diffusionchatapp.eu.diffusion.cloud" by default)
* user: 'user'
* password: 'password'

# Execution

Use Android Studio's Run or Debug features to run the App.
