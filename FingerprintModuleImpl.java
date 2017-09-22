/**
 * 
 */
package com.project.applockproject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.project.applockproject.FingerprintModule.ResponsePacket.ErrorCodes;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

/**
 * @author Mr marios
 *
 */
public class FingerprintModuleImpl extends Thread implements FingerprintModule {

    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    
    byte[] repsonse = new byte[12];
	CommandPacket cp = new CommandPacket();
	ResponsePacket rp = new ResponsePacket();
	ErrorCodes ec = rp.new ErrorCodes();
	
	public FingerprintModuleImpl(BluetoothSocket socket){
		
		mmSocket= socket;
		InputStream tempIn = null;
		OutputStream tempOut= null;
		
		try{
			tempIn = socket.getInputStream();
			tempOut= socket.getOutputStream();
		}catch(IOException e){}
		try {
			mmSocket.connect();
		} catch (IOException e) {
		}
		mmInStream =tempIn;
		mmOutStream =tempOut;
	}
	public void closeConnection(BluetoothSocket socket) throws IOException{
		mmSocket.close();
	}
	/* (non-Javadoc)
	 * @see com.project.applockproject.FingerprintModule#GetPacketBytes()
	 */
	@Override
	public byte[] GetPacketBytes() {
		//ByteObj[] packetbytes= new ByteObj[12];
		 byte[] packetbytes= new byte[12];
		 
		 
		 short cmd = (short) cp.command;
		 cp.theCommand[0] = GetLowByte(cmd);
		 cp.theCommand[1]= GetHighByte(cmd);
		 
		 short checksum = _CalculateChecksum();
		 
		 packetbytes[0] = (byte) cp.COMMAND_START_CODE_1;
		 packetbytes[1] = (byte) cp.COMMAND_START_CODE_2;
		 packetbytes[2] = (byte) cp.COMMAND_DEVICE_ID_1;
		 packetbytes[3] = (byte) cp.COMMAND_DEVICE_ID_2;
		 packetbytes[4] = cp.Parameter[0];
		 packetbytes[5] = cp.Parameter[1];
		 packetbytes[6] = cp.Parameter[2];
		 packetbytes[7] = cp.Parameter[3];
		 packetbytes[8] = cp.theCommand[0];
		 packetbytes[9] = cp.theCommand[1];
		 packetbytes[10] = this.GetLowByte(checksum);
		 packetbytes[11] = this.GetHighByte(checksum);
		 
		return packetbytes;
	}

	/* (non-Javadoc)
	 * @see com.project.applockproject.FingerprintModule#ParameterFromInt(int)
	 */
	@Override
	public void ParameterFromInt(int i) {
		cp.Parameter[0]= (byte) (i & 0x000000ff);
		cp.Parameter[1]= (byte) ((i & 0x0000ff00) >> 8);
		cp.Parameter[2]= (byte) ((i & 0x00ff0000) >> 16);
		cp.Parameter[3]= (byte) ((i & 0xff000000) >> 24);
	}

	/* (non-Javadoc)
	 * @see com.project.applockproject.FingerprintModule#CommandPacket()
	 */
	@Override
	public void CommandPacket() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.project.applockproject.FingerprintModule#_CalculateChecksum()
	 */
	@Override
	public short _CalculateChecksum() {
		short sum= 0;
		
		sum+= cp.COMMAND_START_CODE_1;
		sum+= cp.COMMAND_START_CODE_2;
		sum+= cp.COMMAND_DEVICE_ID_1;
		sum+= cp.COMMAND_DEVICE_ID_2;
		sum+= cp.Parameter[0];
		sum+= cp.Parameter[1];
		sum+= cp.Parameter[2];
		sum+= cp.Parameter[3];
		sum+= cp.theCommand[0];
		sum+= cp.theCommand[1];
		return sum;
	}

	/* (non-Javadoc)
	 * @see com.project.applockproject.FingerprintModule#GetHighByte(short)
	 */
	@Override
	public byte GetHighByte(short w) {
		return (byte) ((w>>8)&0x00FF);
	}

	/* (non-Javadoc)
	 * @see com.project.applockproject.FingerprintModule#GetLowByte(short)
	 */
	@Override
	public byte GetLowByte(short w) {
		return (byte) (w&0x00FF);
	}

	/* (non-Javadoc)
	 * @see com.project.applockproject.FingerprintModule#ParseFromBytes(byte, byte)
	 */
	@Override
	public int ParseFromBytes(byte high, byte low) {
		int e = ec.INVALID;
		if (high == 0x00){
		}
		if (high == 0x10){
			switch(low){
			
			case 0x00: 
				e = ec.NO_ERROR; 
				break;
			case 0x01: 
				e = ec.NACK_TIMEOUT; 
				break;
			case 0x02: 
				e = ec.NACK_INVALID_BAUDRATE; 
				break;		
			case 0x03:
				e = ec.NACK_INVALID_POS; 
				break;			
			case 0x04: 
				e = ec.NACK_IS_NOT_USED; 
				break;		
			case 0x05: 
				e = ec.NACK_IS_ALREADY_USED; 
				break;
			case 0x06: 
				e = ec.NACK_COMM_ERR; 
				break;
			case 0x07: 
				e = ec.NACK_VERIFY_FAILED; 
				break;
			case 0x08: 
				e = ec.NACK_IDENTIFY_FAILED; 
				break;
			case 0x09: 
				e = ec.NACK_DB_IS_FULL; 
				break;
			case 0x0A: 
				e = ec.NACK_DB_IS_EMPTY; 
				break;
			case 0x0B: 
				e = ec.NACK_TURN_ERR; 
				break;
			case 0x0C: 
				e = ec.NACK_BAD_FINGER; 
				break;
			case 0x0D: 
				e = ec.NACK_ENROLL_FAILED; 
				break;
			case 0x0E: 
				e = ec.NACK_IS_NOT_SUPPORTED; 
				break;
			case 0x0F: 
				e = ec.NACK_DEV_ERR; 
				break;
			case 0x10: 
				e = ec.NACK_CAPTURE_CANCELED; 
				break;
			case 0x11: 
				e = ec.NACK_INVALID_PARAM; 
				break;
			case 0x12: 
				e = ec.NACK_FINGER_IS_NOT_PRESSED; 
				break;
			}
		}
		return e;
	}

	/* (non-Javadoc)
	 * @see com.project.applockproject.FingerprintModule#ResponsePacket(com.project.applockproject.FingerprintModule.ByteObj, boolean)
	 */
	@Override
	public void checkResponsePacket(byte[] buffer) {
		if (buffer[8] == 0x30){
			rp.ACK= true; 
		}
		else{
			rp.ACK= false;
		}
		rp.Error = ParseFromBytes(buffer[5], buffer[4]);
		
		rp.ParameterBytes[0] = buffer[4];
		rp.ParameterBytes[1] = buffer[5];
		rp.ParameterBytes[2] = buffer[6];
		rp.ParameterBytes[3] = buffer[7];
		rp.ResponseBytes[0]  = buffer[8];
		rp.ResponseBytes[1]  = buffer[9];
	}

	/* (non-Javadoc)
	 * @see com.project.applockproject.FingerprintModule#IntFromParameter()
	 */
	@Override
	public int IntFromParameter() {
		
		int responseValue = 0;
		
		responseValue = (responseValue << 8) + rp.ParameterBytes[3];
		responseValue = (responseValue << 8) + rp.ParameterBytes[2];
		responseValue = (responseValue << 8) + rp.ParameterBytes[1];
		responseValue = (responseValue << 8) + rp.ParameterBytes[0];
		
		return responseValue;
	}

	/* (non-Javadoc)
	 * @see com.project.applockproject.FingerprintModule#CheckParsing(com.project.applockproject.FingerprintModule.ByteObj, byte, byte, com.project.applockproject.FingerprintModule.CharObj, boolean)
	 */
	@Override
	public boolean CheckParsing(ByteObj b, byte propervalue,
			byte alternatevalue, CharObj varname, boolean UseSerialDebug) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.project.applockproject.FingerprintModule#CalculateChecksum(com.project.applockproject.FingerprintModule.ByteObj, int)
	 */
	@Override
	public short CalculateChecksum(ByteObj buffer, int length) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.project.applockproject.FingerprintModule#GetHighByteE(short)
	 */
	@Override
	public byte GetHighByteE(short w) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.project.applockproject.FingerprintModule#GetLowByteE(short)
	 */
	@Override
	public byte GetLowByteE(short w) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.project.applockproject.FingerprintModule#FPS_GT511C3(java.io.InputStream, java.io.OutputStream)
	 */
	@Override
	public void FPS_GT511C3(InputStream tmpIn, OutputStream tmpOut) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.project.applockproject.FingerprintModule#Open()
	 */
	@Override
	public void Open() throws IOException {
		
		//CommandPacket cp = new CommandPacket(); 	
		cp.command = CommandPacket.Commands.Open;
		cp.Parameter[0]=0x00;
		cp.Parameter[1]=0x00;
		cp.Parameter[2]=0x00;
		cp.Parameter[3]=0x00;
		byte[] packetbytes = this.GetPacketBytes();
		//while(true){
			//if(mmInStream.available()==0){
				SendCommand(packetbytes,12);
			//}
			//else{
				//break;
			//}
		//}
		ResponsePacket rp = this.getResponse();
	}

	/* (non-Javadoc)
	 * @see com.project.applockproject.FingerprintModule#Close()
	 */
	@Override
	public void Close() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.project.applockproject.FingerprintModule#SetLED(boolean)
	 */
	@Override
	public boolean SetLED(boolean on) throws IOException {
		
		cp.command = CommandPacket.Commands.CmosLed;
		if(on){
			cp.Parameter[0]= 0x01;
		}
		else{
			cp.Parameter[0]= 0x00;
		}
		cp.Parameter[1]=0x00;
		cp.Parameter[2]=0x00;
		cp.Parameter[3]=0x00;
		byte[] packetbytes = this.GetPacketBytes();	
		//while(true){
			//if(mmInStream.available()==0){
				SendCommand(packetbytes,12);
			//}
			//else{
				//break;
			//}
		//}

		ResponsePacket rp = this.getResponse();
		boolean retval= true;
		if (rp.ACK==false){
			retval= false;
		}		
		return retval;
	}

	/* (non-Javadoc)
	 * @see com.project.applockproject.FingerprintModule#ChangeBaudRate(int)
	 */
	@Override
	public boolean ChangeBaudRate(int baud) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.project.applockproject.FingerprintModule#GetEnrollCount()
	 */
	@Override
	public int GetEnrollCount() {
		cp.command = CommandPacket.Commands.GetEnrollCount;
		cp.Parameter[0]=0x00;
		cp.Parameter[1]=0x00;
		cp.Parameter[2]=0x00;
		cp.Parameter[3]=0x00;
		byte[] packetbytes = this.GetPacketBytes();
		SendCommand(packetbytes,12);
		ResponsePacket rp = this.getResponse();
		int responseValue = IntFromParameter();
		return responseValue;
	}

	/* (non-Javadoc)
	 * @see com.project.applockproject.FingerprintModule#CheckEnrolled(int)
	 */
	@Override
	public boolean CheckEnrolled(int id) {
		cp.command = CommandPacket.Commands.CheckEnrolled;
		this.ParameterFromInt(id);
		byte[] packetbytes = this.GetPacketBytes();
		SendCommand(packetbytes, 12);
		ResponsePacket rp = this.getResponse();
		boolean responseStatus= true;
		if (rp.ACK==false){
			responseStatus= false;
		}		
		return responseStatus;
	}

	/* (non-Javadoc)
	 * @see com.project.applockproject.FingerprintModule#EnrollStart(int)
	 */
	@Override
	public int EnrollStart(int id) {
		cp.command = CommandPacket.Commands.EnrollStart;
		this.ParameterFromInt(id);
		byte[] packetbytes = this.GetPacketBytes();
		SendCommand(packetbytes, 12);
		ResponsePacket rp = this.getResponse();
		int responseValue = 0;
		if(rp.ACK==false){
			if (rp.Error == ResponsePacket.ErrorCodes.NACK_DB_IS_FULL)responseValue=1;
			if (rp.Error == ResponsePacket.ErrorCodes.NACK_INVALID_POS) responseValue = 2;
			if (rp.Error== ResponsePacket.ErrorCodes.NACK_IS_ALREADY_USED) responseValue = 3;
		}
		return responseValue;
	}

	/* (non-Javadoc)
	 * @see com.project.applockproject.FingerprintModule#Enroll1()
	 */
	@Override
	public int Enroll1() {
		cp.command = CommandPacket.Commands.Enroll1;
		byte[] packetbytes = this.GetPacketBytes();
		SendCommand(packetbytes,12);
		ResponsePacket rp = this.getResponse();
		int responseValue = IntFromParameter();
		if(responseValue<200){
			responseValue=3;
		}
		else{
			responseValue=0;
		}
		if(rp.ACK==false){
			if (rp.Error == ResponsePacket.ErrorCodes.NACK_ENROLL_FAILED)responseValue=1;
			if (rp.Error == ResponsePacket.ErrorCodes.NACK_BAD_FINGER) responseValue = 2;
		}
		if(rp.ACK){
			return 0;
		}
		else{
			return responseValue;
		}
	}

	/* (non-Javadoc)
	 * @see com.project.applockproject.FingerprintModule#Enroll2()
	 */
	@Override
	public int Enroll2() {
		cp.command = CommandPacket.Commands.Enroll2;
		byte[] packetbytes = this.GetPacketBytes();
		SendCommand(packetbytes,12);
		ResponsePacket rp = this.getResponse();
		int responseValue = IntFromParameter();
		if(responseValue<200){
			responseValue=3;
		}
		else{
			responseValue=0;
		}
		if(rp.ACK==false){
			if (rp.Error == ResponsePacket.ErrorCodes.NACK_ENROLL_FAILED)responseValue=1;
			if (rp.Error == ResponsePacket.ErrorCodes.NACK_BAD_FINGER) responseValue = 2;
		}
		if(rp.ACK){
			return 0;
		}
		else{
			return responseValue;
		}
	}

	/* (non-Javadoc)
	 * @see com.project.applockproject.FingerprintModule#Enroll3()
	 */
	@Override
	public int Enroll3() {
		cp.command = CommandPacket.Commands.Enroll3;
		byte[] packetbytes = this.GetPacketBytes();
		SendCommand(packetbytes,12);
		ResponsePacket rp = this.getResponse();
		int responseValue = IntFromParameter();
		if(responseValue<200){
			responseValue=3;
		}
		else{
			responseValue=0;
		}
		if(rp.ACK==false){
			if (rp.Error == ResponsePacket.ErrorCodes.NACK_ENROLL_FAILED)responseValue=1;
			if (rp.Error == ResponsePacket.ErrorCodes.NACK_BAD_FINGER) responseValue = 2;
		}
		if(rp.ACK){
			return 0;
		}
		else{
			return responseValue;
		}
	}

	/* (non-Javadoc)
	 * @see com.project.applockproject.FingerprintModule#IsPressFinger()
	 */
	@Override
	public boolean IsPressFinger() {
		cp.command = CommandPacket.Commands.IsPressFinger;
		byte[] packetbytes = this.GetPacketBytes();
		SendCommand(packetbytes,12);
		ResponsePacket rp = this.getResponse();
		boolean responseStatus= false;
		int parValue = rp.ParameterBytes[0];
		parValue += rp.ParameterBytes[1];
		parValue += rp.ParameterBytes[2];
		parValue += rp.ParameterBytes[3];
		if(parValue==0){
			responseStatus=true;
		}
		return responseStatus;
	}

	/* (non-Javadoc)
	 * @see com.project.applockproject.FingerprintModule#DeleteID(int)
	 */
	@Override
	public boolean DeleteID(int ID) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.project.applockproject.FingerprintModule#DeleteAll()
	 */
	@Override
	public boolean DeleteAll() {
		cp.command = CommandPacket.Commands.DeleteAll;
		byte[] packetbytes = this.GetPacketBytes();
		SendCommand(packetbytes,12);
		ResponsePacket rp = this.getResponse();
		boolean responseStatus = rp.ACK;
		return responseStatus;
	}

	/* (non-Javadoc)
	 * @see com.project.applockproject.FingerprintModule#Verify1_1(int)
	 */
	@Override
	public int Verify1_1(int id) {
		cp.command = CommandPacket.Commands.Verify1_1;
		ParameterFromInt(id);
		byte[] packetbytes = this.GetPacketBytes();
		SendCommand(packetbytes,12);
		ResponsePacket rp = this.getResponse();
		int responseValue = 0;
		if(rp.ACK==false){
			if (rp.Error == ResponsePacket.ErrorCodes.NACK_INVALID_POS)responseValue=1;
			if (rp.Error == ResponsePacket.ErrorCodes.NACK_IS_ALREADY_USED) responseValue = 2;
			if (rp.Error== ResponsePacket.ErrorCodes.NACK_VERIFY_FAILED) responseValue = 3;
		}
		return responseValue;
	}

	/* (non-Javadoc)
	 * @see com.project.applockproject.FingerprintModule#Identify1_N()
	 */
	@Override
	public int Identify1_N() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.project.applockproject.FingerprintModule#CaptureFinger(boolean)
	 */
	@Override
	public boolean CaptureFinger(boolean highquality) {
		cp.command = CommandPacket.Commands.CaptureFinger;
		if(highquality){
			ParameterFromInt(1);
		}
		else{
			ParameterFromInt(0);
		}
		byte[] packetbytes = this.GetPacketBytes();
		SendCommand(packetbytes, 12);
		ResponsePacket rp = this.getResponse();
		boolean responseStatus= rp.ACK;
		return responseStatus;
	}

	/* (non-Javadoc)
	 * @see com.project.applockproject.FingerprintModule#serialPrintHex(byte)
	 */
	@Override
	public void serialPrintHex(byte data) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.project.applockproject.FingerprintModule#SendToSerial(byte[], int)
	 */
	@Override
	public void SendToSerial(byte[] data, int length) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.project.applockproject.FingerprintModule#SendCommand(byte[], int)
	 */
	@Override
	public void SendCommand(byte cmd[], int length) {
		try {
			mmOutStream.write(cmd);
		} catch (IOException e) {}
	}

	@Override
	public ResponsePacket getResponse() {

		byte respSize = 0;
		byte firstByte = 0;
		//byte[] buffer = new byte[12];
		boolean done= false;
		while(done== false){
			
			try {
				firstByte = (byte) mmInStream.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(firstByte==ResponsePacket.COMMAND_START_CODE_1){
				done= true;
			}
		}	
		byte[] buffer = new byte[12];	
		buffer[0] =	firstByte;	
		try {
			respSize = (byte) mmInStream.read(buffer, 1, 11);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		//try {
			//respSize = (byte) mmInStream.read(buffer);	
			//firstByte=buffer[0];
			//} catch (IOException e) {
			//	Log.e("YOUR_APP_LOG_TAG", "I got an error", e);
			//}
		//if(firstByte==ResponsePacket.COMMAND_START_CODE_1){
		//		done= true;
		//}
		
	
		//byte[] repsonse = new byte[12]; 
		//repsonse[0]=firstByte;
		//for(int i= 1; i<12; i++){
			//try {
				//while(mmInStream.available()==0)
					//try {
						//Thread.sleep(10);
					//} catch (InterruptedException e) {}
				//repsonse[i]= (byte) mmInStream.read();
			//} catch (IOException e) {}
		//}
		checkResponsePacket(buffer);
		//ResponsePacket responsePack = new ResponsePacket(repsonse);
		return rp;
	}


}
