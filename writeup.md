# Analysis
###### Jenna Sgarlata (sgarlatajj@msoe.edu) and Andrew Sullivan-Bormann (sullivan-bormannaj@msoe.edu)
| Original Line Number | Fault Description | Bug Description |
| -------------------- | ----------------- | --------------- |
| 84 | not throwing a NullPointerException when a null AudioPlayer was passed into constructor | null AudioPlayer could be passed in |
| 119 | checking for percent change greater than zero, when it should be checking for greater than or equal to 1 | AudioPlayer would play when percent change was between 0 and 1, when it should not be |
| 151 | throwing exception when the current quote was not null, rather than when it was | exception was thrown when state was valid |
| 185 | Throws NullPointerException instead of InvalidAnalysisState | This would cause the program to break if the exception was thrown |
| 187 | Subtracted the change since close from the close | This would result in the incorrect amount from the change since the close |
| 204 | Multiplied by 100000 then divided by 100 after rounding | This gave the percent times 10 instead of the percent |
| Between 219, 220 | No check for if the current quote was null | Could throw an unexpected NullPointerException |
| 220 | Subtracts last trade from itself and returns it | Would always return 0 |

![coverage](/lab7.png)
