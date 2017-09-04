package com.cogito.erm.compliance.compliancecheck.service;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cogito.erm.compliance.compliancecheck.model.Constants;
import com.cogito.erm.compliance.compliancecheck.model.Employee;
import com.cogito.erm.compliance.compliancecheck.model.ExcelCellData;
import com.cogito.erm.compliance.compliancecheck.model.LocationToMailMapper;
import com.cogito.erm.compliance.compliancecheck.repo.EmployeeRepo;
import com.cogito.erm.compliance.compliancecheck.repo.LocationToMailMapperRepo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.cogito.erm.compliance.compliancecheck.model.Constants.EMAIL_HEADER;

/**
 * Created by pavankumarjoshi on 25/07/2017.
 */
@Service
public class ExcelFileReader implements FileReaderIF {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private LocationToMailMapperRepo locationToMailMapperRepo;

    private static final int MAIL_ROW_NUM = 0;
    private static final int HEADER_ROW_NUM = 1;

    private static final Logger Log = LoggerFactory.getLogger(ExcelFileReader.class);

    @Override
    public void read(String path) throws Exception{

        try {
            //String pathFile = "/Users/pavankumarjoshi/Documents/workspace/Playground/ERM/compliancecheck/src/main/resources/input1.xlsx";
            FileInputStream file = new FileInputStream(new File(
                    path));



            List<ExcelCellData> headerCells = new ArrayList<>();

            List<Employee> employees = new ArrayList<>();

            Workbook workbook = WorkbookFactory.create(new File(path));
            //Create Workbook instance holding reference to .xlsx file
            //XSSFWorkbook workbook = ne
            // w XSSFWorkbook(file);

            //Get first/desired sheet from the workbook

            int totalSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < totalSheets; i++) {

                LocationToMailMapper locationToMailMapper = new LocationToMailMapper();
                int excelSheetRowNum = 0;
                int dataStructureRowNum = 0;
                Sheet sheet = workbook.getSheetAt(i);


                Iterator<Row> rowIterator = sheet.iterator();

                while (rowIterator.hasNext()) {

                    Employee employee = new Employee();
                    String sheetName = sheet.getSheetName();
                    employee.setLocation(sheetName);
                    locationToMailMapper.setLocationName(sheetName);
                    int cellNum = 0;
                    Row row = rowIterator.next();
                    //For each row, iterate through all the columns
                    Iterator<Cell> cellIterator = row.cellIterator();


                    while (cellIterator.hasNext()) {
                        Cell cell0 = cellIterator.next();
                        Cell cell = row.getCell(cellNum, Row.RETURN_NULL_AND_BLANK);


                        // Reading all header cells
                        // Assumed header cells are in row 0
                        // Also it is assumed that all the header cells have string values
                        if(excelSheetRowNum == MAIL_ROW_NUM){
                            if(cell!=null && cell.getStringCellValue() != null
                                    && EMAIL_HEADER.equalsIgnoreCase("EMAIL")){

                                Cell emailCell = row.getCell(cellNum + 1);
                                if(emailCell!=null && emailCell.getStringCellValue() != null){
                                    String emailList = emailCell.getStringCellValue();
                                    locationToMailMapper.setEmailAddress(emailList);
                                }
                            }
                            break;
                        }
                        else if (excelSheetRowNum == HEADER_ROW_NUM) {
                            ExcelCellData headerCellData = new ExcelCellData();
                            headerCellData.setColNum(cellNum);
                            headerCellData.setRowNum(dataStructureRowNum);
                            String headerName = (String) cell.getStringCellValue();
                            headerCellData.setValue(headerName);
                            headerCells.add(headerCellData);

                        } else {
                            if (cellNum == getColNum(Constants.SECURITY_EXPIRY, headerCells)) {

                                employee.setSecurityExpiryDate(getCellDateValue(cell));

                            } else if (cellNum == getColNum(Constants.MSIC_EXPIRY, headerCells)) {

                                employee.setMsicExpiryDate(getCellDateValue(cell));

                            } else if (cellNum == getColNum(Constants.FIRST_AID_EXPIRY, headerCells)) {

                                employee.setFirstAidExpiry(getCellDateValue(cell));

                            } else if (cellNum == getColNum(Constants.PA_NSW_IND, headerCells)) {

                                employee.setPaNswInd(getCellDateValue(cell));

                            } else if (cellNum == getColNum(Constants.SPOTLESS_IND, headerCells)) {

                                employee.setSpotlessInd(getCellDateValue(cell));

                            } else if (cellNum == getColNum(Constants.NSW_SECURITY, headerCells)) {

                                employee.setNswSecurity(getCellValue(cell));

                            } else if (cellNum == getColNum(Constants.FIRSTNAME, headerCells)) {
                                //System.out.println("FIRSTNAME " + cell.getStringCellValue());
                                employee.setFirstName(getCellValue(cell));
                            } else if (cellNum == getColNum(Constants.SURNAME, headerCells)) {
                                //System.out.println("LASTNAME " + cell.getStringCellValue());
                                employee.setLastName(getCellValue(cell));
                            } else if (cellNum == getColNum(Constants.MSICNO, headerCells)) {
                                //System.out.println("MSICNO " + cell.getStringCellValue());
                                employee.setMsicNo(getCellValue(cell));
                            } else if (cellNum == getColNum(Constants.CLASS, headerCells)) {
                                //System.out.println("CLASS " + cell.getStringCellValue());
                                employee.setSecurityClass(getCellValue(cell));
                            } else if (cellNum == getColNum(Constants.RSA, headerCells)) {
                                //System.out.println("RSA " + cell.getStringCellValue());
                                employee.setRsa(getCellValue(cell));
                            } else if (cellNum == getColNum(Constants.EMAILID, headerCells)) {
                                //System.out.println("email id " + cell.getStringCellValue());
                                employee.setEmailId(getCellValue(cell));
                            } else if (cellNum == getColNum(Constants.PFSO, headerCells)) {
                                //System.out.println("PFSO " + cell.getStringCellValue());
                                employee.setPfso(getCellValue(cell));
                            } else if (cellNum == getColNum(Constants.DESC, headerCells)) {
                                //System.out.println("DESC " + cell.getStringCellValue());
                                //employee.setDesc(cell.getStringCellValue());
                            } else if (cellNum == getColNum(Constants.PHONENUMBER, headerCells)) {
                                //System.out.println("PHONENUMBER " + cell.getStringCellValue());
                                employee.setPhoneNumber(getCellValue(cell));
                            }
                        }
                        cellNum++;
                    }
                    System.out.println("Saving Employee " + employee.getFirstName() + " " + employee.getLastName());
                    if (excelSheetRowNum != MAIL_ROW_NUM
                            && excelSheetRowNum != HEADER_ROW_NUM) {
                        if (employee.getFirstName() != null && employee.getLastName() != null) {
                            employeeRepo.save(employee);
                        }
                    }

                    excelSheetRowNum++;
                    dataStructureRowNum++;
                }
                locationToMailMapperRepo.save(locationToMailMapper);
            }


        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
            Log.debug(ex.getMessage());
            throw ex;
        }

    }

    private String getCellValue(Cell cell){

        if(cell != null) {
            if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                Double numericCellValue = cell.getNumericCellValue();
                Long val = numericCellValue.longValue();
                String s = String.valueOf(val);
                return s;

            } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                return cell.getStringCellValue();
            }
        }
        return null;
    }
    private String getCellDateValue(Cell cell){
        if(cell != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date dateCellValue = cell.getDateCellValue();
                    if (dateCellValue != null) {
                        String format = sdf.format(dateCellValue);
                        if (format != null) {
                            String[] split = format.split(" ");
                            return (split[0]);
                        }
                    }
                }
            }
            else if(cell.getCellType() == Cell.CELL_TYPE_STRING) {
                String stringCellValue = cell.getStringCellValue();
                return stringCellValue;
            }
        }
        return null;
    }


    private int getColNum(String colName, List<ExcelCellData> headerCells) {
        for (ExcelCellData cell : headerCells) {
            if(((String)cell.getValue()).trim().equalsIgnoreCase(colName.trim()))
            {
                return cell.getColNum();
            }
        }
        return -1;
    }

}
