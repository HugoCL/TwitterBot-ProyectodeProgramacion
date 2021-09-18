# TwitterBot, a Twitter Desktop Client built with Java

TwitterBot is a group project for "Programming Project" course at Universidad de Talca. RO-BOT was our bot's name in Twitter, and you can find him here: [RO-BOT, Ramos Overflow BOT (@BotOverflow) / Twitter](https://twitter.com/BotOverflow)

## :dart: Goals of the app

 1. Build a Desktop Client with Java that emulates the features of the Twitter web version. Features such as seeing a feed with all the related tweets, sending Tweets, liking, and retweeting them. We also had to implement the visualization of profiles, and the ability to follow other profiles. Finally, we had to also implement Direct Messages functionality.
 2. Implement Oauth so a user can login in the app and see the content of the account.
 3. Add automations to our app (that's what the name came for), such as auto-reply when the account that was logged in received a Tweet or a DM with certain hashtag, so the app could reply automatically with a response.
 4. Implement a system that could filter or block spam and aggressive behavior in both Tweets and DM's and reply accordingly.

## :computer: Technical information about the app

 1. The app was built using Java (Backend) and JavaFX (Frontend).
 2. We used TwitterAPI, together with Twitter4j (a library for Java to access core functionality from Twitter) to accomplish the basic functionality of our app.
 3. Once we reached the moment of filtering spam or aggresive behavior, we faced 2 options: Create our own database of words and try to match every Tweet and DM (which was very inefficient) or go an extra mile and try to implement an API that could help us with this job. We chose the latter and implemented the [Perspective API](https://www.perspectiveapi.com/), a Jigsaw API that harness the power of AI to detect toxicity in natural language, helping us in detecting almost all the aggresive and spammy comments we tried on our app.

## :eyes: Take a look at the app 

![Direct Messages](https://storage.googleapis.com/hugo-utalca-projects/twitter-bot/Direct%20Messages.png)

![Feed](https://storage.googleapis.com/hugo-utalca-projects/twitter-bot/Feed.png)

![Follow](https://storage.googleapis.com/hugo-utalca-projects/twitter-bot/Follow%20section.png)

![Lading and Login](https://storage.googleapis.com/hugo-utalca-projects/twitter-bot/Landing.png)

![Tweets](https://storage.googleapis.com/hugo-utalca-projects/twitter-bot/Tweet%20Interface.png)
