# Twitch Emote Tracker
Track statistics about emotes in the chat!
Use your [Twitch](https://www.twitch.tv/) account and a valid OAuth to connect with any stream as a chat bot and track emotes in chat.

![Twitch Emote Tracker Launch GUI](/docs/launch-display.png)

## Twitch Chat Commands
Chat users can query the number of times a tracked emote has been posted in chat using the !emotecount command.
For example: "!emotecount Kappa" will post in the chat the number of times the Kappa emote has been posted.

![Twitch Emote Tracker Data Table](/docs/data-table-display.png)

### Build
You can build an executable .jar by using gradle's jar task. At the base of this project run:
```
gradle jar
```
The .jar is created under the /build/libs/ folder

### Run
Run the .jar using the command
```
java -jar twitch_emote_tracker-1.0.0.jar
```

### Instructions
- Enter the name of the Twitch channel that you want the bot to join (ex. ninja).
- To add a new emote to track, enter the emote text, and then press the *Add* button.
- To remove an emote, select it within the list of added emotes, and then press the *Remove* button.
- You must enter your Twitch account nickname (which may differ from your Twitch channel name) and a valid OAuth
token for your Twitch account in order to properly connect and join the chat.
- Once all the fields are filled out, press the *Launch* button to launch the bot.

#### Help
- Fields will indicate improper inputs by setting the text to red.
