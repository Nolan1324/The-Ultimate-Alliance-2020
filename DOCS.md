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
