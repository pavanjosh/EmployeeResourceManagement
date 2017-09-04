package com.cogito.erm.compliance.compliancecheck.controller;

import com.cogito.erm.compliance.compliancecheck.service.FileReaderIF;
import com.cogito.erm.compliance.compliancecheck.service.SchedulerService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by pavankumarjoshi on 26/07/2017.
 */
@Controller
public class FileUplaodController {

    @Autowired
    private FileReaderIF fileReader;

    @Autowired
    SchedulerService schedulerService;


    @Value("${uploaded.folder}")
    private String UPLOADED_FOLDER;

    @Value("${uploaded.filename}")
    private String UPLOADED_FILENAME;

    @Value("${uploaded.file.extension}")
    private String UPLOADED_FILE_EXTENSION;


    @GetMapping("/")
    public String index() {
        return "upload";
    }

    @RequestMapping(value = "/upload", method = {GET,POST})
    public String upload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:upload";
        }

        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + UPLOADED_FILENAME + "-" + LocalDate.now() + UPLOADED_FILE_EXTENSION);
            Files.write(path, bytes);
            fileReader.read(path.toString());
            redirectAttributes.addFlashAttribute("message", "You successfully uploaded '" + file.getOriginalFilename() + "'");
            schedulerService.scanForDates();
        }
        catch (Exception ex){
            ex.printStackTrace();
            return ex.getMessage();
        }
        return "success";
    }
}
