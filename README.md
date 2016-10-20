# RainAlarm

## Synopsis
RainAlarm has been my 2nd Android app. I learned a lot with this project.
It is an app that shows a notification a few hours before it starts to rain.

## Motivation
I love learn new things so that I decided  to do this app.  

## APIs I used
Weather API
https://openweathermap.org/api

## How it works
The user inputs the name of the city he wants to recive an alarm.
Once the user inputs the information, an alarm is set for every X hours which will execute the service.
The service connects to the API and if it is necesari, builds a notification to tell  the user that is going to rain.
It is also necesari a service that detects device boot to set the first alarm because once the device is turned off, all the alarms are removed.

## What I learned
I learned a lot about Android:
- How services work
- How to manage API querys
- How to build notifications
- How to set alarms
- How to detect system events
- etc.
