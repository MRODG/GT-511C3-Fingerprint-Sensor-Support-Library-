/**
 * 
 */
package com.project.applockproject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Mr marios
 *
 */
public interface FingerprintModule {
	
	class ByteObj {
	    public byte value;
		}
	class CharObj {
	    public char value;
	}
//---------------------------------------------------Command Packet---------------------------------------------------

	public class CommandPacket{
			
		public final class Commands{
			
			public final static int NotSet				= 0x00;// Default value for enum. Scanner will return error if sent this.
			public final static int Open 				= 0x01;// Open Initialization
			public final static int Close				= 0x02;// Close Termination
			public final static int UsbInternalCheck	= 0x03;// UsbInternalCheck Check if the connected USB device is valid
			public final static int ChangeEBaudRate		= 0x04;// ChangeBaudrate Change UART baud rate
			public final static int SetIAPMode			= 0x05;// SetIAPMode Enter IAP Mode In this mode, FW Upgrade is available
			public final static int CmosLed				= 0x12;// CmosLed Control CMOS LED
			public final static int GetEnrollCount		= 0x20;// Get enrolled fingerprint count
			public final static int	CheckEnrolled		= 0x21;// Check whether the specified ID is already enrolled
			public final static int	EnrollStart			= 0x22;// Start an enrollment
			public final static int	Enroll1				= 0x23;// Make 1st template for an enrollment
			public final static int	Enroll2				= 0x24;// Make 2nd template for an enrollment
			public final static int	Enroll3				= 0x25;// Make 3rd template for an enrollment, merge three templates into one template, save merged template to the database
			public final static int IsPressFinger		= 0x26;// Check if a finger is placed on the sensor
			public final static int	DeleteID			= 0x40;// Delete the fingerprint with the specified ID
			public final static int DeleteAll			= 0x41;// Delete all fingerprints from the database
			public final static int Verify1_1			= 0x50;// Verification of the capture fingerprint image with the specified ID
			public final static int Identify1_N			= 0x51;// Identification of the capture fingerprint image with the database
			public final static int VerifyTemplate1_1	= 0x52;// Verification of a fingerprint template with the specified ID
			public final static int IdentifyTemplate1_N	= 0x53;// Identification of a fingerprint template with the database
			public final static int CaptureFinger		= 0x60;// Capture a fingerprint image(256x256) from the sensor
			public final static int MakeTemplate		= 0x61;// Make template for transmission
			public final static int GetImage			= 0x62;// Download the captured fingerprint image(256x256)
			public final static int GetRawImage			= 0x63;// Capture & Download raw fingerprint image(320x240)
			public final static int GetTemplate			= 0x70;// Download the template of the specified ID
			public final static int SetTemplate			= 0x71;// Upload the template of the specified ID
			public final static int GetDatabaseStart	= 0x72;// Start database download, obsolete
			public final static int GetDatabaseEnd		= 0x73;// End database download, obsolete
			public final static int UpgradeFirmware		= 0x80;// Not supported
			public final static int UpgradeISOCDImage	= 0x81;// Not supported
			public final static int Ack					= 0x30;// Acknowledge.
			public final static int Nack				= 0x31;// Non-acknowledge
		}
		
		public int command;
		public byte[] Parameter = new byte[4];
		
		static int COMMAND_START_CODE_1 = 0x55;	// Static byte to mark the beginning of a command packet	-	never changes
		static int COMMAND_START_CODE_2 = 0xAA;	// Static byte to mark the beginning of a command packet	-	never changes
		static int COMMAND_DEVICE_ID_1 = 0x01;	// Device ID Byte 1 (lesser byte)							-	theoretically never changes
		static int COMMAND_DEVICE_ID_2 = 0x00;	// Device ID Byte 2 (greater byte)							-	theoretically never changes
		byte[] theCommand = new byte[2];// Command 2 bytes
		

		}
		public byte[] GetPacketBytes();	
		void ParameterFromInt(int i);
		public void CommandPacket();
		
		short _CalculateChecksum();						// Checksum is calculated using byte addition
		byte GetHighByte(short w);						
		byte GetLowByte(short w);
//---------------------------------------------------Response Packet---------------------------------------------------

	public class ResponsePacket{
		public class ErrorCodes{
			
			public final static int NO_ERROR 					= 0x0000;	// Default value. no error
			public final static int NACK_TIMEOUT				= 0x1001;	// Obsolete, capture timeout
			public final static int NACK_INVALID_BAUDRATE		= 0x1002;	// Obsolete, Invalid serial baud rate
			public final static int NACK_INVALID_POS			= 0x1003;	// The specified ID is not between 0~199
			public final static int NACK_IS_NOT_USED			= 0x1004;	// The specified ID is not used
			public final static int NACK_IS_ALREADY_USED		= 0x1005;	// The specified ID is already used
			public final static int NACK_COMM_ERR				= 0x1006;	// Communication Error
			public final static int NACK_VERIFY_FAILED			= 0x1007;	// 1:1 Verification Failure
			public final static int NACK_IDENTIFY_FAILED		= 0x1008;	// 1:N Identification Failure
			public final static int NACK_DB_IS_FULL				= 0x1009;	// The database is full
			public final static int NACK_DB_IS_EMPTY			= 0x100A;	// The database is empty
			public final static int NACK_TURN_ERR				= 0x100B;	// Obsolete, Invalid order of the enrollment (The order was not as: EnrollStart -> Enroll1 -> Enroll2 -> Enroll3)
			public final static int NACK_BAD_FINGER				= 0x100C;	// Too bad fingerprint
			public final static int NACK_ENROLL_FAILED			= 0x100D;	// Enrollment Failure
			public final static int NACK_IS_NOT_SUPPORTED		= 0x100E;	// The specified command is not supported
			public final static int NACK_DEV_ERR				= 0x100F;	// Device Error, especially if Crypto-Chip is trouble
			public final static int NACK_CAPTURE_CANCELED		= 0x1010;	// Obsolete, The capturing is canceled
			public final static int NACK_INVALID_PARAM			= 0x1011;	// Invalid parameter
			public final static int NACK_FINGER_IS_NOT_PRESSED	= 0x1012;	// Finger is not pressed
			public final static int INVALID						= 0XFFFF;	// Used when parsing fails			
		}
		public int Error;
		public byte[] RawBytes= new byte[12];
		public byte[] ParameterBytes= new byte[4];
		public byte[] ResponseBytes= new byte[2];
		boolean ACK;
		
		static int COMMAND_START_CODE_1 = 0x55;	// Static byte to mark the beginning of a command packet	-	never changes
		static int COMMAND_START_CODE_2 = 0xAA;	// Static byte to mark the beginning of a command packet	-	never changes
		static int COMMAND_DEVICE_ID_1 = 0x01;	// Device ID Byte 1 (lesser byte)							-	theoretically never changes
		static int COMMAND_DEVICE_ID_2 = 0x00;	// Device ID Byte 2 (greater byte)							-	theoretically never changes
	}
	int ParseFromBytes(byte high, byte low);
	
	//public ResponsePacket ResponsePacket(byte buffer);
	
	int IntFromParameter();
	
	boolean CheckParsing(ByteObj b, byte propervalue, byte alternatevalue, CharObj varname, boolean UseSerialDebug);
	short CalculateChecksum(ByteObj buffer, int length);
	byte GetHighByteE(short w);						
	byte GetLowByteE(short w);
	void checkResponsePacket(byte[] buffer);
	
	//---------------------------------------------------FPS_GT511C3 module---------------------------------------------------
	
	
		
	boolean UseSerialDebug=true;	
	public void FPS_GT511C3(InputStream tmpIn , OutputStream tmpOut );
	//Initialises the device and gets ready for commands
	void Open() throws IOException;
	// Does not actually do anything (according to the datasheet)
	// I implemented open, so had to do closed too
	void Close();
	// Turns on or off the LED backlight
	// LED must be on to see fingerprints
	// Parameter: true turns on the backlight, false turns it off
	// Returns: True if successful, false if not
	boolean SetLED(boolean on) throws IOException;
	// Changes the baud rate of the connection
	// Parameter: 9600 - 115200
	// Returns: True if success, false if invalid baud
	// NOTE: Untested (don't have a logic level changer and a voltage divider is too slow)
	boolean ChangeBaudRate(int baud);
	// Gets the number of enrolled fingerprints
	// Return: The total number of enrolled fingerprints
	int GetEnrollCount();
	// checks to see if the ID number is in use or not
	// Parameter: 0-199
	// Return: True if the ID number is enrolled, false if not
	boolean CheckEnrolled(int id);
	// Starts the Enrollment Process
	// Parameter: 0-199
	// Return:
	//	0 - ACK
	//	1 - Database is full
	//	2 - Invalid Position
	//	3 - Position(ID) is already used
	int EnrollStart(int id);
	// Gets the first scan of an enrollment
	// Return: 
	//	0 - ACK
	//	1 - Enroll Failed
	//	2 - Bad finger
	//	3 - ID in use
	int Enroll1();
	// Gets the Second scan of an enrollment
	// Return: 
	//	0 - ACK
	//	1 - Enroll Failed
	//	2 - Bad finger
	//	3 - ID in use
	int Enroll2();
	// Gets the Third scan of an enrollment
	// Finishes Enrollment
	// Return: 
	//	0 - ACK
	//	1 - Enroll Failed
	//	2 - Bad finger
	//	3 - ID in use
	int Enroll3();
	// Checks to see if a finger is pressed on the FPS
	// Return: true if finger pressed, false if not
	boolean IsPressFinger();
	// Deletes the specified ID (enrollment) from the database
	// Returns: true if successful, false if position invalid
	boolean DeleteID(int ID);
	// Deletes all IDs (enrollments) from the database
	// Returns: true if successful, false if db is empty
	boolean DeleteAll();
	// Checks the currently pressed finger against a specific ID
	// Parameter: 0-199 (id number to be checked)
	// Returns:
	//	0 - Verified OK (the correct finger)
	//	1 - Invalid Position
	//	2 - ID is not in use
	//	3 - Verified FALSE (not the correct finger)
	int Verify1_1(int id);
	// Checks the currently pressed finger against all enrolled fingerprints
	// Returns:
	//	0-199: Verified against the specified ID (found, and here is the ID number)
	//	200: Failed to find the fingerprint in the database
	int Identify1_N();
	// Captures the currently pressed finger into onboard ram
	// Parameter: true for high quality image(slower), false for low quality image (faster)
	// Generally, use high quality for enrollment, and low quality for verification/identification
	// Returns: True if ok, false if no finger pressed
	boolean CaptureFinger(boolean highquality);
	
	void serialPrintHex(byte data);
	void SendToSerial(byte data[], int length);
	
	void SendCommand(byte cmd[], int length);
	ResponsePacket getResponse() throws IOException, InterruptedException;
	InputStream dataIn=null;
	OutputStream dateOut=null;

	
	
}
