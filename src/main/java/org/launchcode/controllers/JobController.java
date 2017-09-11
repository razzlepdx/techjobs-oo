package org.launchcode.controllers;

import org.launchcode.models.*;
import org.launchcode.models.forms.JobForm;
import org.launchcode.models.data.JobData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping(value = "job")
public class JobController {

    private JobData jobData = JobData.getInstance();

    // The detail display for a given Job at URLs like /job?id=17
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, int id) {

        // TODO #1 - get the Job with the given ID and pass it into the view
        Job jobById = jobData.findById(id);

        model.addAttribute("job", jobById);
        return "job-detail";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new JobForm());
        return "new-job";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @Valid JobForm jobForm, Errors errors) {

        // TODO #6 - Validate the JobForm model, and if valid, create a
        // new Job and add it to the jobData data store. Then
        // redirect to the job detail view for the new Job.

        // check for blank job name field
        // if name is empty, redirect to the new job form and display errors
        if (jobForm.getName().length() == 0) {
            model.addAttribute("errors", errors);
            return "new-job";
        }

        //Get all values from the jobForm and create a new Job()
        String name = jobForm.getName();
        Employer employer = jobData.getEmployers().findById(jobForm.getEmployerId());
        Location location = jobData.getLocations().findById(jobForm.getLocationId());
        CoreCompetency coreCompetency = jobData.getCoreCompetencies().findById(jobForm.getCoreCompetencyId());
        PositionType positionType = jobData.getPositionTypes().findById(jobForm.getPositionTypeId());

        Job newJob = new Job(name, employer, location, positionType, coreCompetency);

        // add freshly created job to jobData
        jobData.add(newJob);


        // display new job on the job detail page
        Job newJobId = jobData.findById(newJob.getId());
        Integer idNum = newJob.getId();
        model.addAttribute("job", newJobId);
        return "redirect:/job?id=" + idNum;

    }
}
