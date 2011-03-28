CREATE TABLE employees(
	username VARCHAR(30) NOT NULL,
	name VARCHAR(60),
	password VARCHAR(20),
	calendarId INTEGER,
	CONSTRAINT pk_employeesUsername PRIMARY KEY (username)
);


CREATE TABLE rooms(
	name VARCHAR(30) NOT NULL,
	size INTEGER,
	description CLOB,
	CONSTRAINT pk_roomsName PRIMARY KEY(name)
);	

CREATE TABLE appointments(
	id INTEGER NOT NULL,
	employee VARCHAR(30) NOT NULL,
	addedDate TIMESTAMP NOT NULL,
	startDate TIMESTAMP NOT NULL,
	endDate TIMESTAMP NOT NULL,
	description CLOB, --character large object, kan vi klare oss med varchar?
	location VARCHAR(60),
	CONSTRAINT pk_appointmentsId PRIMARY KEY(id),
	CONSTRAINT fk_employeeAppointment FOREIGN KEY employeeId
		REFERENCES employees(username);
);


CREATE TABLE meetings(
	id INTEGER NOT NULL,
	room VARCHAR(30) NOT NULL,
	CONSTRAINT pk_meetingsID PRIMARY KEY(id)
		REFERENCES appointments(id), --litt usikker på hvordan "arv" fungerer i sql...
	CONSTRAINT fk_roomMeeting FOREIGN KEY room
		REFERENCES rooms(name)
);

--attendance mangler

