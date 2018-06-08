package ru.innopolis.stc9.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.innopolis.stc9.pojo.*;
import ru.innopolis.stc9.service.IProgramService;
import ru.innopolis.stc9.service.ISpecialityService;
import ru.innopolis.stc9.service.ISubjectService;
import ru.innopolis.stc9.service.ProgramService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.List;

@Controller
public class ProgramController extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ProgramController.class);
    
    @Autowired
    private IProgramService service;
    @Autowired
    private ISpecialityService specService;
    @Autowired
    private ISubjectService subjService;

    @RequestMapping(value = "/addOrUpdate", method = RequestMethod.GET)

    public String addOrUpdate(HttpServletRequest request, Model model) {
        if (model.containsAttribute("program")) {
            model.addAttribute("action", "update");
            model.addAttribute("id", request.getParameter("id"));
        } else {
            model.addAttribute("action", "add");
        }
        List<Speciality> specList = specService.getAll();
        model.addAttribute("roleList",specList);
        List<Subject> subjList = subjService.getAll();
        model.addAttribute("roleList",specList);
        
        return "/addOrUpdate";
    }

    @RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
    public String addOrUpdateProgram(HttpServletRequest request,
                                    @RequestAttribute String id,
                                    @RequestAttribute String action,
                                    @RequestAttribute String specialty,
                                    @RequestAttribute String semester,
                                    @RequestAttribute String subject,
                                    @RequestAttribute String hours, Model model) {

        if (action.equals("add")) {
            Program program = new Program(Integer.parseInt(specialty)
                                        , Integer.parseInt(semester)
                                        , Integer.parseInt(subject)
                                        , Integer.parseInt(hours));
            service.add(program);
        } else {
            if (action.equals("update")) {
                Program program = new Program( Integer.parseInt(id)
                                             , Integer.parseInt(specialty)
                                             , Integer.parseInt(semester)
                                             , Integer.parseInt(subject)
                                             , Integer.parseInt(hours));
                service.update(program);
            }
        }
        return "redirect:programAll";
    }

    @RequestMapping(value = "/deleteProgram", method = RequestMethod.GET)
    public String deleteProgram(HttpServletRequest request,
                               @RequestAttribute String id, Model model) {
        service.deleteById(Long.parseLong(id));
        return ("redirect:programAll");
    }

    @RequestMapping(value = "/programAll", method = RequestMethod.GET)
    public String getAll(HttpServletRequest request, Model model) {
        List<Program> programList = service.getAll();
        if (programList != null) {
            model.addAttribute("programList", programList);
            return "/programList";
        } else {
            return "index";
        }
    }

    @RequestMapping(value = "/updateProgram", method = RequestMethod.GET)
    public String updateProgram(HttpServletRequest request,
                               @RequestAttribute String id, Model model) {
        List<Speciality> specList = specService.getAll();
        List<Subject> subjList = subjService.getAll();
        List<Integer> semList =null;
        List<Integer> hourList =null;

        for (int i=0; i<100; i++) {
            semList.add(i);
            hourList.add(i);
        }

        model.addAttribute("specList", specList);
        model.addAttribute("subjList", subjList);
        model.addAttribute("specList", semList);
        model.addAttribute("subjList", hourList);

        model.addAttribute("program", service.getById(Long.parseLong(id)));
        model.addAttribute("action", "update");
        return ("/addOrUpdate");
    }

    @RequestMapping(value = "/program", method = RequestMethod.GET)
    public String getProgram(HttpServletRequest request,
                            @RequestAttribute String id, Model model) {
        Program program = service.getById(Long.parseLong(id));

        String specName = specService.getById(program.getSpecialty()).getName();
        String subjName = subjService.getById(program.getSubject()).getName();
        String semCount = String.valueOf(program.getSemester());
        String hourCount = String.valueOf(program.getHours());

        model.addAttribute("program", program);
        model.addAttribute("spec", specName);
        model.addAttribute("semester", semCount);
        model.addAttribute("subject", subjName);
        model.addAttribute("hours", hourCount);

        return "/getProgram";
    }
    
   
}
