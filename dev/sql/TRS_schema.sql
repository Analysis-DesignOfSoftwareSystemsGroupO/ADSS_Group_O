SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

-- Create Tables
CREATE TABLE IF NOT EXISTS Driveres_Licenece (
    DriverID character(32) NOT NULL,
    Licence character(32) NOT NULL,
    PRIMARY KEY (DriverID, Licence)
);

CREATE TABLE IF NOT EXISTS Drivers (
    id character(32) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS ProductListDocument (
    ProductListDocumentID integer NOT NULL,
    TransportID integer DEFAULT -1,
    totalweight integer,
    aproximatedArrivaleTime time,
    DestinationSiteName character(32),
    Date date,
    PRIMARY KEY (ProductListDocumentID)
);

CREATE TABLE IF NOT EXISTS ProductListdocument_Products (
    ProductListDocumentId integer NOT NULL,
    ProductQuantety integer DEFAULT 0,
    WeightPerUnit integer,
    ProductSerialNumber character(32) NOT NULL,
    PRIMARY KEY (ProductListDocumentId, ProductSerialNumber)
);

CREATE TABLE IF NOT EXISTS Transports (
    id integer NOT NULL,
    Date date,
    maximum_weight integer,
    TruckPN character(12),
    DriverID character(12),
    departure_time time,
    Source_site_name character(32),
    is_sent boolean,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Transports_ProductListdocument (
    TransportId integer NOT NULL,
    ProductListDocumentId integer NOT NULL,
    PRIMARY KEY (TransportId, ProductListDocumentId)
);

CREATE TABLE IF NOT EXISTS TruckAvailability (
    Date date NOT NULL,
    TruckPN character(12) NOT NULL,
    PRIMARY KEY (Date, TruckPN)
);

CREATE TABLE IF NOT EXISTS Trucks (
    maxWeight integer,
    LicenceReq character(32),
    PlateNumber character(32) NOT NULL,
    PRIMARY KEY (PlateNumber)
);

-- Foreign Keys
ALTER TABLE Driveres_Licenece
    ADD CONSTRAINT DriverId_Fkey FOREIGN KEY (DriverID) REFERENCES Drivers(id);

ALTER TABLE ProductListdocument_Products
    ADD CONSTRAINT ForeignKeyPLDid FOREIGN KEY (ProductListDocumentId) REFERENCES ProductListDocument(ProductListDocumentID);

ALTER TABLE ProductListDocument
    ADD CONSTRAINT TransportIDForeignKey FOREIGN KEY (TransportID) REFERENCES Transports(id);

ALTER TABLE Transports
    ADD CONSTRAINT DriverID_FK FOREIGN KEY (DriverID) REFERENCES Drivers(id),
    ADD CONSTRAINT TruckPN_FK FOREIGN KEY (TruckPN) REFERENCES Trucks(PlateNumber);

ALTER TABLE TruckAvailability
    ADD CONSTRAINT TruckPN_FK FOREIGN KEY (TruckPN) REFERENCES Trucks(PlateNumber);

ALTER TABLE Transports_ProductListdocument
    ADD CONSTRAINT pldID_FK FOREIGN KEY (ProductListDocumentId) REFERENCES ProductListDocument(ProductListDocumentID),
    ADD CONSTRAINT transportID_FK FOREIGN KEY (TransportId) REFERENCES Transports(id);
