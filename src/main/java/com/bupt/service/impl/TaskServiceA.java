package com.bupt.service.impl;
import com.bupt.repository.EvaluationRepository;
import com.bupt.repository.ReviewRepository;
import com.bupt.repository.StudentSubmissionRepository;
import com.bupt.repository.TaskRepository;
import com.bupt.mapper.TeacherTaskMapper;
import com.bupt.pojo.*;
import com.bupt.service.TaskService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TaskServiceA implements TaskService {

    @Autowired
    private TaskProducer taskProducer;

    @Autowired
    private TeacherTaskMapper teacherTaskMapper;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private StudentSubmissionRepository studentSubmissionRepository;
    @Autowired
    private EvaluationRepository evaluationRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private TaskCompletionListener taskCompletionListener;

    @Value("${data.video.path}")
    private String videoHome;
    @Value("${data.teacherTask.path}")
    private String teacherTaskHome;
    private ObjectMapper mapper = new ObjectMapper();
    @Value("${data.word.path}")
    private String textHome;
    @Value("${data.ppt.path}")
    private String pptHome;
    @Value("${data.audio.path}")
    private String audioHome;

    private Map<String,String> filePathMap= new HashMap<>();
    private List<String> videoReview = new ArrayList<>(List.of(
            "演讲视频没有拍摄到有效沟通区域（含头顶到腰部，包含手势等肢体语言），请联系老师，重新提交作业",
            "演讲视频背景噪声较大，需要提升拍摄质量，请联系老师，重新提交作业",
            "演讲视频画面模糊，需要提升拍摄质量，请联系老师，重新提交作业",
            "演讲整体效果趋于平淡。建议加强语调变化、眼神交流等提升演讲效果。再接再厉哟",
            "演讲过程较为自信，能较好地运用肢体语言和眼神交流等完成演讲。表现很棒！",
            "演讲过程较为自信，精神状态佳，有感染力。表现很好！",
            "演讲过程自信大方，能通过眼神与手势有效沟通。表现很棒！",
            "演讲过程自信从容，注重语调变化、眼神交流，能合理运用肢体语言。表现非常棒！"
            ));
    private Map<String,String> fileType = new HashMap<>();
    @javax.annotation.PostConstruct
    public void initMap() {
        filePathMap.put("视频",".mp4");
        filePathMap.put("PPT",".pptx");
        filePathMap.put("演讲稿",".docx");
        filePathMap.put("音频",".mp3");
    }
    @javax.annotation.PostConstruct
    public void initMap2() {
        fileType.put("视频","video");
        fileType.put("PPT","ppt");
        fileType.put("演讲稿","docx");
        fileType.put("音频","audio");
    }
    public StudentTask getTask(){
        return null;
    }
    public String getNewFileName(MultipartFile file){
        String[] temFileList = file.getOriginalFilename().split("\\.");
        String fileSuffix = '.' + temFileList[temFileList.length-1];
        String newFileName = UUID.randomUUID() + fileSuffix;
        return newFileName;
    } // 服务器端存储的文件名字
    public int uploadTask(MultipartFile file) throws IOException {
        String newFileName = this.getNewFileName(file);
        file.transferTo(new File(this.videoHome + newFileName));
        return 0;
    }
    public int uploadTeacherTask(TeacherTask teacherTask, MultipartFile file) throws IOException {
        String newFileName = "";
        if (file!=null) {
            newFileName = this.getNewFileName(file);
            file.transferTo(new File(this.teacherTaskHome + newFileName));
        }
        teacherTask.setSubmitContentJson(mapper.writeValueAsString(teacherTask.getSubmitContent()));
        teacherTask.setWeightsJson(mapper.writeValueAsString(teacherTask.getWeights()));
        teacherTask.setReviewSettingsJson(mapper.writeValueAsString(teacherTask.getReviewSettings()));
        teacherTask.setReviewProcessJson(mapper.writeValueAsString(teacherTask.getReviewProcess()));
        teacherTask.setFilePath(newFileName);
        int result = teacherTaskMapper.insertTask(teacherTask);
        return result;
    }

    public int deleteTeacherTask(List<Integer> deleteList) throws IOException{
        for(int id:deleteList){
            String filePath = teacherTaskMapper.deleteFilePath(id);
            File file = new File(teacherTaskHome+filePath);
            if(file.exists()) {
                boolean deleted = file.delete();
            }
            teacherTaskMapper.deleteTeacherTask(id);
        }
        return 0;
    }

    @Override
    public PageBean<TeacherTask> getTeacherTasks(int page, int pageSize, Integer teacherId, String taskName) {
        PageHelper.startPage(page,pageSize);
        List<TeacherTask> taskList = teacherTaskMapper.getTeacherTasks(teacherId,taskName);
        Page<TeacherTask> p = (Page<TeacherTask>) taskList;
        PageBean pageBean = new PageBean(p.getTotal(),p.getResult());
        return pageBean;
    }

    public ReviewTask setReviewTask(Integer studentId,String taskId,Integer reviewerId, String reviewType){
        ReviewTask reviewTask = new ReviewTask(); //评价任务
        reviewTask.setStudentId(studentId);
        reviewTask.setTaskId(taskId);
        reviewTask.setReviewerId(reviewerId);
        reviewTask.setReviewType(reviewType);
        return reviewTask;
    }
    public List<Integer> autoSetMulAccess(List<Integer> shuffleNumbers){
        Collections.shuffle(shuffleNumbers);
        Random random = new Random();
        int randomNumber = 1 + random.nextInt(shuffleNumbers.size()-1);
        List<Integer> mulNumbers = new ArrayList<>(shuffleNumbers);
        for(int i=0;i<mulNumbers.size();i++){
            mulNumbers.set((i+randomNumber)%shuffleNumbers.size(),shuffleNumbers.get(i));
        }
        return mulNumbers;
    }

    //创建任务
    @Override
    public Task createTask(Task task, MultipartFile file) throws IOException {
        task.setMulAccessIds(new HashMap<>());
        return taskRepository.save(task);
    }
    //根据任务拥有者查询任务列表
    @Override
    public List<Task> getTasksByOwnerId(Integer ownerId) {

        return taskRepository.findByOwnerId(ownerId);
    }
    public PageBean<ReviewTask> getReviewTaskByRewId(int page,int pageSize,Integer reviewerId){
        PageHelper.startPage(page,pageSize);
        List<ReviewTask> reviewTaskList = reviewRepository.findByReviewerId(reviewerId);
        PageInfo<ReviewTask> p = new PageInfo<>(reviewTaskList);
        PageBean pageBean = new PageBean(p.getTotal(),p.getList());
        return pageBean;
    }

    public PageBean<StudentSubmission> getSubmissionsByStuId(int page,int pageSize,Integer studentId){
        PageHelper.startPage(page,pageSize);
        List<StudentSubmission>  submissions = studentSubmissionRepository.findByStudentId(studentId);
        PageInfo<StudentSubmission> p = new PageInfo<>(submissions);
        PageBean pageBean = new PageBean(p.getTotal(),p.getList());
        return pageBean;
    }

    public Evaluation getEvaluationBySubId(String submissionId){
        return evaluationRepository.getEvaluationBySubmissionId(submissionId).get();
    }

    @Override
    public Evaluation evaluateTask(String taskId) {

        return null;
    }

    public StudentSubmission uploadTask(String submissionId, MultipartFile file, String submissionType) throws IOException {
        StudentSubmission studentSubmission = studentSubmissionRepository.findById(submissionId).get();
        String newFileName = "";
        newFileName = this.getNewFileName(file);
        file.transferTo(new File(this.filePathMap.get(submissionType) + newFileName));
        List<StudentSubmission.TaskInfo> taskInfos = studentSubmission.getTaskInfos();
        for(StudentSubmission.TaskInfo taskInfo:taskInfos){
            if(taskInfo.getSubmissionType().equals(submissionType)){
                if (taskInfo.getFileName() != null){
                    String filePath = this.filePathMap.get(submissionType) + taskInfo.getFileName();
                    File file1 = new File(filePath);
                    try {
                        file1.delete(); //删除之前提交的文件
                    } catch (Exception e){
                        log.error(String.valueOf(e));
                    }
                }

                taskInfo.setFileName(newFileName);
                taskInfo.setFinished(true);
                taskInfo.setSubmittedTime(LocalDateTime.now());
                break;
            }
        }
        studentSubmission.setTaskInfos(taskInfos);
        return studentSubmissionRepository.save(studentSubmission);
    } //上传任务
    @Transactional
    @Override
    public StudentSubmission uploadVideoTask(String submissionId, MultipartFile videoFile) throws IOException {//提交视频任务
        StudentSubmission studentSubmission = uploadTask(submissionId,videoFile,"视频");
        String existAudioFile = studentSubmission.getTaskInfos().stream()
                .filter(taskInfo -> taskInfo.getSubmissionType().equals("音频"))
                .findFirst().get()
                .getFileName();
        if(existAudioFile!=null){
            File file = new File(audioHome + existAudioFile);
            if(file.delete()){
                log.info("文件删除成功");
            }else{
                log.error(file.getAbsolutePath() + "doesn't exist");
            }
        }
        String videoFileName = studentSubmission.getTaskInfos().stream()
                .filter(taskInfo -> taskInfo.getSubmissionType().equals("视频"))
                .findFirst().get()
                .getFileName();
        String videoFilePath = videoHome + videoFileName;
        String audioFileName = getNewFileName(videoFile);
        String newAudioName = audioFileName.replace(".mp4",".mp3");
        //extractAudio(videoFilePath,audioHome + newAudioName);
        taskProducer.extraAudioFromVide(videoFileName,newAudioName);
        StudentSubmission.TaskInfo audioTaskInfo = studentSubmission.getTaskInfos().stream()
                .filter(taskInfo -> taskInfo.getSubmissionType().equals("音频"))
                .findFirst().get();
        audioTaskInfo.setSubmittedTime(LocalDateTime.now());
        audioTaskInfo.setFinished(Boolean.TRUE);
        audioTaskInfo.setFileName(newAudioName);

        return studentSubmissionRepository.save(studentSubmission);
    }
    //need 重点验证
    @Override
    public StudentSubmission uploadTextTask(String submissionId, MultipartFile textFile) throws IOException {
        StudentSubmission studentSubmission = uploadTask(submissionId,textFile,"演讲稿");
        return studentSubmission;
    }
    //获得机评成绩
    public Evaluation getMachineGrade(Integer stuId,String taskId,String reviewType) throws IOException, ExecutionException, InterruptedException {
        StudentSubmission studentSubmission = studentSubmissionRepository.findByTaskIdAndStudentId(taskId,stuId).get();
        String fileNameWord =  studentSubmission.getTaskInfos().stream()
                .filter(taskInfo -> taskInfo.getSubmissionType().equals("演讲稿"))
                .findFirst().get().getFileName();
        String reviewTaskId = UUID.randomUUID().toString();
        //声明变量
        String reason = null;
        Integer grade = null;
        List<Map<String,String>> evaluationDimensions = null;
        //发送请求机评处理消息
        if(reviewType.equals("演讲稿")){
            taskProducer.getMachineGradeWord(reviewTaskId,fileNameWord); //提交任务
            CompletableFuture<Object> result = new CompletableFuture<>();
            taskCompletionListener.addPendingTask(reviewTaskId,result);  //创建异步任务
            result.orTimeout(300, TimeUnit.SECONDS)
                    .exceptionally(ex ->{
                        ex.printStackTrace();
                        return null;
                    });
            JsonNode resultJson = (JsonNode) result.get();
            //文本操作流程
            reason = resultJson.get("reason").asText();
            grade = resultJson.get("grade").asInt();
            evaluationDimensions= mapper.convertValue(
                    resultJson.get("review"),
                    new TypeReference<List<Map<String, String>>>() {}
            );
        }
        else if ("音频".equals(reviewType)){
            String fileNameAudio =  studentSubmission.getTaskInfos().stream()
                    .filter(taskInfo -> taskInfo.getSubmissionType().equals("音频"))
                    .findFirst().get().getFileName();
            CompletableFuture<Object> result = new CompletableFuture<>();
            taskCompletionListener.addPendingTask(reviewTaskId,result);
            taskProducer.getMachineGradeAudio(reviewTaskId,fileNameWord,fileNameAudio);
            result.orTimeout(300, TimeUnit.SECONDS)
                    .exceptionally(ex ->{
                        ex.printStackTrace();
                        return null;
                    });
            JsonNode resultJson = (JsonNode) result.get();
            System.out.println(resultJson);
            Map<String,String> content_dict = mapper.convertValue(
                    resultJson.get("content_dict"),
                    new TypeReference<Map<String, String>>() {}
            );
            reason = mapper.writeValueAsString(content_dict);
            grade = resultJson.get("total_score").asInt();
            evaluationDimensions= mapper.convertValue(
                    resultJson.get("evaluationDimensions"),
                    new TypeReference<List<Map<String, String>>>() {}
            );
        }
        else if ("PPT".equals(reviewType)){
            String fileNamePPtx = studentSubmission.getTaskInfos().stream()
                    .filter((taskInfo -> taskInfo.getSubmissionType().equals("PPT")))
                    .findFirst().get().getFileName();
            CompletableFuture<Object> result = new CompletableFuture<>();
            taskCompletionListener.addPendingTask(reviewTaskId,result);
            taskProducer.getMachineGradePPT(reviewTaskId,fileNamePPtx);
            result.orTimeout(300, TimeUnit.SECONDS)
                    .exceptionally(ex ->{
                        ex.printStackTrace();
                        return null;
                    });
            JsonNode resultJson = (JsonNode) result.get();
            reason = resultJson.get("summary").asText();
            evaluationDimensions= mapper.convertValue(
                    resultJson.get("evaluationDimensions"),
                    new TypeReference<List<Map<String, String>>>() {}
            );
        }


        Evaluation evaluation = evaluationRepository.getEvaluationByTaskIdAndStudentId(taskId,stuId).get();
        Evaluation.EvaluationType MachineEvaluation = evaluation.getEvaluationTypes().stream()
                .filter(evaluationType -> "机评".equals(evaluationType.getEvaluationMethod()))
                .findFirst().get();
        Evaluation.EvaluationContent evaluationContent = MachineEvaluation.getEvaluationContents().stream()
                .filter(evaluationContent1 -> reviewType.equals(evaluationContent1.getEvaluationContent()))
                .findFirst().get();

        //设置成绩
        evaluationContent.setGrade(grade);
        evaluationContent.setFinished(Boolean.TRUE);
        evaluationContent.setEvaluationTitle(reason);
        evaluationContent.setEvaluationDimensions(evaluationDimensions);

        return evaluationRepository.save(evaluation);
    }

    @Override
    public Evaluation getMachineGradeVideo(Integer stuId, String taskId) throws JsonProcessingException, ExecutionException, InterruptedException {
        StudentSubmission studentSubmission = studentSubmissionRepository.findByTaskIdAndStudentId(taskId,stuId).get();
        String fileNameVideo =  studentSubmission.getTaskInfos().stream()
                .filter(taskInfo -> taskInfo.getSubmissionType().equals("视频"))
                .findFirst().get().getFileName();
        String reviewTaskId = UUID.randomUUID().toString();
        taskProducer.getMachineGradeVideo(reviewTaskId,fileNameVideo);

        CompletableFuture<Object> result = new CompletableFuture<>();
        taskCompletionListener.addPendingTask(reviewTaskId,result);
        result.orTimeout(300, TimeUnit.SECONDS)
                .exceptionally(ex ->{
                    ex.printStackTrace();
                    return null;
                });
        JsonNode resultJson = (JsonNode) result.get();
        //文本操作流程
        Integer grade = resultJson.get("grade").asInt();
        String review = videoReview.get((int) (grade/12.5));

        Evaluation evaluation = evaluationRepository.getEvaluationByTaskIdAndStudentId(taskId,stuId).get();
        Evaluation.EvaluationType MachineEvaluation = evaluation.getEvaluationTypes().stream()
                .filter(evaluationType -> "机评".equals(evaluationType.getEvaluationMethod()))
                .findFirst().get();
        Evaluation.EvaluationContent evaluationContent = MachineEvaluation.getEvaluationContents().stream()
                .filter(evaluationContent1 -> "视频".equals(evaluationContent1.getEvaluationContent()))
                .findFirst().get();

        evaluationContent.setFinished(Boolean.TRUE);
        evaluationContent.setGrade(grade);
        evaluationContent.setEvaluationTitle(review);
        return evaluationRepository.save(evaluation);
    }

    //创建学生成绩表
    public int createEvaluations(Task task,List<StudentSubmission> studentSubmissions){
        List<Evaluation> evaluations = new ArrayList<>();
        String taskId = task.getId();
        for(StudentSubmission studentSubmission:studentSubmissions){
            Evaluation evaluation = new Evaluation();
            evaluation.setTaskId(taskId);
            evaluation.setStudentId(studentSubmission.getStudentId());
            evaluation.setSubmissionId(studentSubmission.getId());
            List<Evaluation.EvaluationType> evaluationTypes = getEvaluationTypes(task);
            evaluation.setEvaluationTypes(evaluationTypes);
            evaluations.add(evaluation);
        }
        List<Evaluation> uploadEvaluations = evaluationRepository.saveAll(evaluations);
        return uploadEvaluations.size();
    }

    @NotNull
    private static List<Evaluation.EvaluationType> getEvaluationTypes(Task task) {
        List<Evaluation.EvaluationType> evaluationTypes = new ArrayList<>();
        for(String reviewType: task.getEvaluationMethods()){
            Evaluation.EvaluationType evaluationType = new Evaluation.EvaluationType();
            evaluationType.setEvaluationMethod(reviewType);
            List<Evaluation.EvaluationContent> evaluationContents = new ArrayList<>();
            for(String submissionType: task.getSubmissionTypes()){
                evaluationContents.add(new Evaluation.EvaluationContent(submissionType,Boolean.FALSE,"",null,new ArrayList<>()));
            }
            evaluationType.setEvaluationContents(evaluationContents);
            evaluationTypes.add(evaluationType);
        }
        return evaluationTypes;
    }

    //分配学生任务 同时设置学生提交与成绩
    @Override
    @Transactional
    public Task assignStudents(String taskId, List<Integer> studentIds) { //创建任务 studentIds默认不为空 //支持增量分配
        Task task = taskRepository.findById(taskId).get();
        List<Integer> newAssignList = new ArrayList<>();
        if(task.getAssignedStudents()!=null){ //合并分配任务
            newAssignList.addAll(task.getAssignedStudents());
        }
        newAssignList.addAll(studentIds);
        task.setAssignedStudents(newAssignList);
        List<StudentSubmission> studentSubmissions = new ArrayList<>();
        Map<String,Integer> mulAccessIds = task.getMulAccessIds();
        for(Integer stuId:studentIds){
            mulAccessIds.put(String.valueOf(stuId),-1);
            StudentSubmission studentSubmission = new StudentSubmission();
            studentSubmission.setTaskId(task.getId());
            studentSubmission.setStudentId(stuId);
            studentSubmission.setFinished(false);
            List<StudentSubmission.TaskInfo> taskInfos = new ArrayList<>();
            for(String submitType:task.getSubmissionTypes()){
                String fileName = "/" + stuId.toString() + "/" + taskId + "/" + fileType.get(submitType) +"/"+task.getDeadline() + filePathMap.get(submitType);
                taskInfos.add(
                        new StudentSubmission.TaskInfo(submitType,fileName,null,false)
                );
            }
            studentSubmission.setTaskInfos(taskInfos);
            studentSubmissions.add(studentSubmission);
        }
        //设置任务提交
        if(task.getEvaluationMethods().contains("自评")){
            List<ReviewTask> reviewTasks = new ArrayList<>();
            for(int stuId:studentIds){
                ReviewTask reviewTask = new ReviewTask();
                reviewTask.setStudentId(stuId);
                reviewTask.setReviewType("自评");
                reviewTask.setReviewerId(stuId);
                reviewTask.setTaskId(task.getId());
                List<ReviewTask.ReviewClass> reviews = new ArrayList<>();
                for(String submissionType:task.getSubmissionTypes()){
                    reviews.add(new ReviewTask.ReviewClass(submissionType,false));
                }
                reviewTask.setReviews(reviews);
                reviewTasks.add(reviewTask);
            }
            reviewRepository.saveAll(reviewTasks);
        }
        task.setMulAccessIds(mulAccessIds);
        List<StudentSubmission> saveSubmissions = studentSubmissionRepository.saveAll(studentSubmissions);
        int n = createEvaluations(task,saveSubmissions);
        return taskRepository.save(task);
    }
    //分配评价任务 + 分配成绩表


    //分配师评任务
    @Override
    public List<ReviewTask> setReviewTeachers(String taskId,List<Integer> teacherIds){
        Task task = taskRepository.findById(taskId).get();
        List<Integer> newReviewTeachers = new ArrayList<>();
        if(task.getReviewTeachers()!=null){
            newReviewTeachers.addAll(task.getReviewTeachers());
        }
        newReviewTeachers.addAll(teacherIds);
        task.setReviewTeachers(newReviewTeachers);
        List<ReviewTask> reviewTasks = new ArrayList<>();
        for(int teacherId:teacherIds){
            for(int stuId:task.getAssignedStudents()){
                ReviewTask reviewTask = new ReviewTask();
                reviewTask.setReviewerId(teacherId);
                reviewTask.setReviewType("师评");
                reviewTask.setStudentId(stuId);
                reviewTask.setTaskId(task.getId());
                List<ReviewTask.ReviewClass> reviews = new ArrayList<>();
                for (String submissionType: task.getSubmissionTypes()){
                    reviews.add(new ReviewTask.ReviewClass(submissionType,false));
                }
                reviewTask.setReviews(reviews);
                reviewTasks.add(reviewTask);
            }
        }
        taskRepository.save(task); //更新评价老师
        reviewRepository.saveAll(reviewTasks);
        return reviewRepository.saveAll(reviewTasks);
    }

    //分配互评任务
    @Override
    @Transactional
    public List<ReviewTask> setReviewStudents(String taskId, List<Integer> stuIds, List<Integer> allocateStudents, boolean reAllocate){

        Task task = taskRepository.findById(taskId).get();
        List<Integer> newReviewStudents = new ArrayList<>();
        Map<String,Integer> mulAccessIds = task.getMulAccessIds();
        List<Integer> awaitingAssignment;

        if(!allocateStudents.isEmpty()){ //已经确认分配关系
            newReviewStudents.addAll(allocateStudents);
            awaitingAssignment = stuIds;
        }else{  //未指定分配关系 全部重新分配
            if(reAllocate){
                newReviewStudents = autoSetMulAccess(task.getAssignedStudents()); //新的未分配的学生
                awaitingAssignment = task.getAssignedStudents();
            }else{ //不重新分配 且为一键分配
                newReviewStudents.addAll(autoSetMulAccess(stuIds));
                awaitingAssignment = stuIds;
            }
        }
        List<ReviewTask> reviewTasks = new ArrayList<>();
        for(int i=0;i<awaitingAssignment.size();i++){
            mulAccessIds.put(String.valueOf(awaitingAssignment.get(i)),newReviewStudents.get(i));
            ReviewTask reviewTask = reviewRepository.findByTaskIdAndAndStudentIdAndAndReviewType(taskId,awaitingAssignment.get(i),"互评")
                    .orElse(new ReviewTask());//不存在则创建
            reviewTask.setStudentId(awaitingAssignment.get(i));
            reviewTask.setReviewType("互评");
            reviewTask.setTaskId(task.getId());
            reviewTask.setReviewerId(newReviewStudents.get(i));
            List<ReviewTask.ReviewClass> reviews = new ArrayList<>();
            for (String submissionType: task.getSubmissionTypes()){
                reviews.add(new ReviewTask.ReviewClass(submissionType,false));
            }
            reviewTask.setReviews(reviews);
            reviewTasks.add(reviewTask);
        }

        taskRepository.save(task); //更新评价老师
        return reviewRepository.saveAll(reviewTasks);
    }

    public Evaluation getEvaluationByTaskIdAndStuId(String taskId,Integer stuId){
        Evaluation evaluation = evaluationRepository.getEvaluationByTaskIdAndStudentId(taskId,stuId)
                .orElseThrow(() -> {
                    String errorMessage = "task or stuId not found for taskId:" + taskId + " and stuId:" +stuId;
                    log.error(errorMessage);
                    return new IllegalArgumentException("task or stuId not found");});
        return evaluation;
    }
    //删除分配记录
    @Override
    @Transactional
    public String deleteAllocated(List<String> reviewTaskIds) { //删除互评任务
        if(reviewTaskIds.isEmpty()){
            return "error:列表为空！";
        }
        List<ReviewTask> reviewTasks = reviewRepository.getReviewTasksByIdIn(reviewTaskIds);
        String taskId = reviewTasks.get(0).getTaskId();
        Task task = taskRepository.findById(taskId).get();
        Map<String,Integer> mulAccessIds = task.getMulAccessIds();
        for(ReviewTask reviewTask:reviewTasks){
            int studentId = reviewTask.getStudentId(); //被评价学生Id
            mulAccessIds.put(String.valueOf(studentId),-1);
        }
        taskRepository.save(task);
        reviewRepository.deleteAll(reviewTasks);
        return "成功删除";
    }
    @Transactional
    @Override
    public Task deleteAssigned(String taskId, List<Integer> studentIds) {
        List<Evaluation> evaluations= evaluationRepository.getEvaluationByTaskIdAndStudentIdIn(taskId,studentIds);
        List<ReviewTask> reviewTasks = reviewRepository.findByTaskIdAndStudentIdIn(taskId,studentIds);
        List<StudentSubmission> studentSubmissions = studentSubmissionRepository.findByTaskIdAndStudentIdIn(taskId,studentIds);
        Task task = taskRepository.findById(taskId).get();
        List<Integer> assignedStudents = task.getAssignedStudents();
        Map<String,Integer> mulAccessIds = task.getMulAccessIds();
        for(int studentId:studentIds){
            mulAccessIds.remove(String.valueOf(studentId));
            assignedStudents.remove(Integer.valueOf(studentId));
        }
        taskRepository.save(task);
        evaluationRepository.deleteAll(evaluations);
        reviewRepository.deleteAll(reviewTasks);
        studentSubmissionRepository.deleteAll(studentSubmissions);
        return taskRepository.save(task);
    }

    @Override
    public StudentSubmission uploadPPtTask(MultipartFile PPtFile, String submissionId) throws IOException {
        return uploadTask(submissionId,PPtFile,"PPT");
    }

    //删除分配任务
    @Override
    @Transactional
    public String deleteTask(String taskId){
        Task task = taskRepository.findById(taskId).get();
        List<Integer> studentIds = task.getAssignedStudents();
        task = deleteAssigned(task.getId(),studentIds);
        taskRepository.delete(task);
        return "成功删除";
    }

    @Override
    public Task setTaskEvaluation(String taskId, List<EvaluationDimension> evaluationDimensions) {
        Task task = taskRepository.findById(taskId).get();
        task.setEvaluationDimensions(evaluationDimensions);
        return taskRepository.save(task);
    }
    @Transactional
    @Override
    public Evaluation setGrade(String taskId, Integer stuId,Evaluation.EvaluationType evaluationType) {
        Evaluation evaluation = evaluationRepository.getEvaluationByTaskIdAndStudentId(taskId,stuId).get();
        ReviewTask reviewTask = reviewRepository.findByTaskIdAndAndStudentIdAndAndReviewType(taskId,stuId,evaluationType.getEvaluationMethod()).get();

        //修改评价结果
        for(int i=0;i<evaluation.getEvaluationTypes().size();i++){
            Evaluation.EvaluationType evaluationType1 = evaluation.getEvaluationTypes().get(i);
            if(evaluationType1.getEvaluationMethod().equals(evaluationType.getEvaluationMethod())){
                evaluation.getEvaluationTypes().set(i,evaluationType);
                break;
            }
        }

        for(Evaluation.EvaluationContent evaluationContent:evaluationType.getEvaluationContents()){
            reviewTask.getReviews().stream()
                    .filter(reviewClass -> reviewClass.getReviewContent().equals(evaluationContent.getEvaluationContent()))
                    .forEach(reviewClass -> reviewClass.setFinished(evaluationContent.getFinished()));
        }
        reviewRepository.save(reviewTask);

        return evaluationRepository.save(evaluation);
    }

    @Override
    public Task getTaskInfoById(String taskId) {
        Task task = taskRepository.findById(taskId).get();
        return task;
    }

    @Override
    public StudentSubmission getSubmissionInfoById(String submissionId) {
        return studentSubmissionRepository.findById(submissionId).get();
    }

    @Override
    public PageBean getAllGradesByTaskId(String taskId,int page,int pageSize) {
        PageHelper.startPage(page,pageSize);
        List<Evaluation>  evaluations = evaluationRepository.getEvaluationByTaskId(taskId);
        PageInfo<Evaluation> p = new PageInfo<>(evaluations);
        PageBean pageBean = new PageBean(p.getTotal(),p.getList());
        return pageBean;
    }

    @Override
    public PageBean getAllSubmitsByTaskId(String taskId, int page, int pageSize) {
        PageHelper.startPage(page,pageSize);
        List<StudentSubmission>  submissions = studentSubmissionRepository.findStudentSubmissionByTaskId(taskId);
        PageInfo<StudentSubmission> p = new PageInfo<>(submissions);
        PageBean pageBean = new PageBean(p.getTotal(),p.getList());
        return pageBean;
    }

}
