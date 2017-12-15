# changestreams
Demo for Orange County MUG Dec-5-2017
![Demo](https://raw.githubusercontent.com/snarvaez/changestreams/master/demo_slide.png)

Steps:
1. Setup a MongoDB 3.6 replica set
2. Build using `mvn package`
3. Run using `java -jar ./bin/changestreams.jar --host MONGODB_CONNECTION_STRING`
4. Install [mgeneratejs](https://github.com/rueckstiess/mgeneratejs)
5. Generate data simultaneously using the V1 and V2 Device Event templates:\
`mgeneratejs -n 50000 36_DeviceEventsV1.json | ./mongoimport -d IoT -c DeviceEvents -j 2` \
`mgeneratejs -n 50000 36_DeviceEventsV2.json | ./mongoimport -d IoT -c DeviceEvents -j 2`
6. As data is commited to the replica set, the change streams java program will print out events to the console  
7. After completion, browse the `IoT` database and the `DeviceEvents` and `Devices` collections using [MongoDB Compass](https://www.mongodb.com/products/compass). 
8. The `Devices` collection will contain only 1 entry per Device with variations in the json schema
