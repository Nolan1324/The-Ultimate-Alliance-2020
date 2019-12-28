# Game Dependent Files
The majority of this application works for any FRC game. However, there are certain files pertaining to the year's specific FRC game that must be changed every season.
- Match Scouting
  - [model/gamedata/Data.java](/app/src/main/java/com/nolankuza/theultimatealliance/model/gamedata/Data.java) - contains all of the data points to be collected, and how to convert that data to and from a raw (parcel) form. Easy to edit.
  - [scout/AutoFragment](/app/src/main/java/com/nolankuza/theultimatealliance/scout/AutoFragment.java) and [layout/fragment_auto.xml](/app/src/main/res/layout/fragment_auto.xml) - backend and frontend for the inital autonomous scouting interface.
   - [scout/AutoScoreFragment](/app/src/main/java/com/nolankuza/theultimatealliance/scout/AutoScoreFragment.java) and [layout/fragment_auto_score.xml](/app/src/main/res/layout/fragment_auto_score.xml) - backend and frontend for the autonomous scoring scouting interface. Generally the same as the teleop interface, except it saves to different data points
   - [scout/TeleopFragment](/app/src/main/java/com/nolankuza/theultimatealliance/scout/TeleopFragment.java) and [layout/fragment_teleop.xml](/app/src/main/res/layout/fragment_teleop.xml) - backend and frontend for the teleop scouting interface. The backend is very simple; it just reads and writes from counters. The frontend can be edited visually in Android Studio.
   - [scout/EndGameFragment](/app/src/main/java/com/nolankuza/theultimatealliance/scout/EndGameFragment.java) and [layout/fragment_end_game.xml](/app/src/main/res/layout/fragment_end_game.xml) - backend and frontend for the end game scouting interface.

I am currently working to minimize the content in these files and reduce the amount of manual effort required to edit these files.

# Data Communication Protocol
## Data codes
Codes to identify the type of data sent

| Description | Value |
| --- | --- |
| Game Data | `0x01` |
| Pit Data | `0x02` |
| Matches | `0x03` |
| Teams | `0x04` |
| Assignments | `0x05` |
| Settings | `0x06` |
| Students | `0x07` |
| Playoff Data | `0x08` |

## Send message
Either sent by the master at the beginning of a communication or sent to the master as response to a "request message".

| Bytes | Description | Value |
| --- | --- | --- |
| 0 | Direction code | `0x00` |
| 1| Is more<br>(is the another send message coming?) | `0x00` - No<br>`0x01` - Yes |
| 2 | Data code | [See data codes table](#data-codes) |
| 3-6 | Length | `n` |
| 7-`(n+7)` | Data | Raw Parcel Array Data |
| `(n+8)`-`(n+11)` | Event Key Length (data code = 0x05) | `m` |
| `(n+12)`-`(n+12+m)` | Event Key (data code = 0x05) | Ex: `2018_milak` |

## Request message
Sent by the master at the beginning of a communication to request data to be sent back.
When a slave tablet receives this message, it is expected to send a "send message" for each of the data codes listed in the "request message".

| Bytes | Description | Value |
| --- | --- | --- |
| 0 | Direction code | `0x01` |
| 1| Is more<br>(will the master send data afterwards?) | `0x00` - No<br>`0x01` - Yes |
| 2-`(n+2)` | List of data codes (types of data to request)<br>`n` = Length of list | [See data codes table](#data-codes) |
| `(n+3)` | Null byte | `0x00` |
