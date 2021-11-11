# Twitch Emote Tracker
Track statistics about emotes in the chat!
Use your [Twitch](https://www.twitch.tv/) account and a valid [OAuth Password](https://twitchapps.com/tmi/) to connect with any stream as a chat bot and track emotes in chat.

## Twitch Chat Commands
Chat users can query the number of times a tracked emote has been posted in chat using the !emotecount command.
For example: "!emotecount Kappa" will post in the chat the number of times the Kappa emote has been posted.

### Build
You can build an executable .jar using gradle's jar task
```
gradle jar
```
The .jar is created under the /build/libs/ folder
Run the .jar using the command
```
java -jar twitch_emote_tracker-1.0.0.jar
```
