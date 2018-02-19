package utils.resources;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.*;

public class DataProviderClass extends BaseClass{
	
	public static Logger log = LogManager.getLogger(BaseClass.class.getName());
	private XSSFWorkbook wb;

	public String[] getData() throws IOException{
		
		int rowCount;
		String str;
		
		log.info("Get property value of inputfile");
		String inputFile = prop.getProperty("filepath");
		log.info("Input file path:"+inputFile);
		FileInputStream fis = new FileInputStream(inputFile);
		
		log.info("Create Workbook object");
		wb = new XSSFWorkbook(fis);
		log.info("Create sheet objcet");
		XSSFSheet sh = wb.getSheet("Sheet1");
		
		log.info("Get no. of rows");
		rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
		log.info("Row Count:"+rowCount);
		
		String[] result = new String[rowCount];
		
		for(int i=1; i<rowCount+1; i++){
			XSSFRow row= sh.getRow(i);
			str = row.getCell(0).getStringCellValue();
			result[i-1] = str;
		}
		log.info("No. of entries in file in DPC class:"+result.length);
		return result;
	}
}
	
