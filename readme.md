# 메인로직
![main_logic](https://user-images.githubusercontent.com/84568097/188052178-41cd16df-5aa7-4df1-98d6-c4a8ea83c920.PNG)


<br/>
<br/>
<br/>

# [메인 페이지](./schoolManagement/src/main/java/com/example/schoolmanagement/controller/)
![signup](https://user-images.githubusercontent.com/84568097/188051214-2cc77f7d-92c3-4fdf-afcd-1957d2bae512.PNG)
<br/>
<br/>
<br/>

# [관리자 페이지](./schoolManagement/src/main/java/com/example/schoolmanagement/web/siteManager/controller/)

### 관리자 로그인&메인 페이지
![admin_index](https://user-images.githubusercontent.com/84568097/187846854-3c02ac6e-2aa3-4a33-8ece-6b49e11e561c.PNG)
<br/>
<br/>
<br/>

### 관리자 리스트 조회 페이지
* 학교 리스트 조회
* 선생님 리스트 조회
* 학생 리스트 조회
![admin_list](https://user-images.githubusercontent.com/84568097/187847055-38ad558f-b9ff-492f-b18e-a53419631821.PNG)
<br/>
<br/>
<br/>


# [선생님 페이지](./schoolManagement/src/main/java/com/example/schoolmanagement/web/siteTeacher/controller/)
### 선생님 메인 페이지
* 학생 리스트 조회
* 시험지 리스트 조회
![teacher_index](https://user-images.githubusercontent.com/84568097/187847708-ce87b432-c09e-4029-b3af-960ff320219f.PNG)
<br/>
<br/>
<br/>
### 선생님 시험지 및 등록 페이지
* 문제 생성 및 추가
* 문제 수정 및 삭제
![teacher_problem](https://user-images.githubusercontent.com/84568097/187847929-32c5eb85-a7e4-4184-874e-873ae94f25f8.PNG)
<br/>
<br/>
<br/>

### 선생님 시험지 배포 페이지
* 배포를 누르면 담당 학생들에게 시험지가 배포
* 배포 후 시험지 문제 수정, 삭제 불가
* 배포 후 시험지 문제 추가는 가능
* 배포 후에 학생들이 시험지를 다 풀고 제출했을 시,<br/>
 선생님이 문제를 추가로 더 냈을 때, 시험지 상태는 결과에서 배포로 변경  
 이 때 배포를 누르면 학생들은 추가한 문제에 대해서만 다시 풀이 가능
![teacher_result](https://user-images.githubusercontent.com/84568097/188052564-c3d162a2-5991-4327-afb7-0d11b7c7e2ba.PNG)

<br/>
<br/>
<br/>

# [학생 페이지](./schoolManagement/src/main/java/com/example/schoolmanagement/web/siteStudy/controller/)
### 학생 메인 페이지
![study_index](https://user-images.githubusercontent.com/84568097/187848791-1c32de2a-2f71-413b-b8dc-054f0a0d2d58.PNG)
<br/>
<br/>
<br/>

### 학생 시험 페이지
* 풀지 않은 시험지 확인
* 문제 풀이
* 시험 점수 확인
![study_test](https://user-images.githubusercontent.com/84568097/187848953-2b0836cc-3868-42dd-bbfb-4935c39b248a.PNG)
<br/>
<br/>
<br/>

### 학생 점수 결과 페이지
* 풀이가 완료된 시험지 확인
* 시험지 점수 확인
![study_result](https://user-images.githubusercontent.com/84568097/187849280-9560bf65-6734-48e4-a13e-a52704d98bfb.PNG)
