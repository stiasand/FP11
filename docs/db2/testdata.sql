INSERT INTO employees
VALUES(
	'kristian',
	'Kristian Nordbø Svendsen',
	'hemmelig'
);

INSERT INTO appointments (employee, addedDate, startDate, endDate, description, location)
VALUES(
	'kristian',
	CURRENT TIMESTAMP,
	CURRENT TIMESTAMP,
	CURRENT TIMESTAMP,
	'Skrive rapport, lang kjedelig beskrivelse',
	'På kontoret'
);
