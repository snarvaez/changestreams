# changestreams
Demo for Orange County MUG Dec-5-2017

Steps:
1. Setup a MongoDB 3.6 replica set
2. Build using `mvn package`
3. Run using `java -jar ./bin/changestreams.jar`
4. Install mgeneratejs from https://github.com/rueckstiess/mgeneratejs
5. Generate data simultaneously using templates:\
`mgeneratejs -n 50000 36_DeviceEventsV1.json | ./mongoimport -d IoT -c DeviceEvents`\
`mgeneratejs -n 50000 36_DeviceEventsV2.json | ./mongoimport -d IoT -c DeviceEvents`\
6. Browse `IoT` database and `DeviceEvents` and `Devices` collections using MongoDB Compass


