/**
 * Created by zhaoyu on 2018/5/7.
 */
function changeDate() {
    newDate = DatePicker.val();
    sessionStorage.setItem('currentDate', newDate);
    console.log(sessionStorage.getItem('currentDate'));
}