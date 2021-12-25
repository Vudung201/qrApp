<?php
   if(isset($_POST['maNV'])){
    require_once "connect.php";
    $maNV = $_POST['maNV'];
    $sqlselect =  "SELECT * FROM `nhanvien` WHERE maNV = '$maNV';";
    $result = array();
    $result['data'] = array();
    $execute = $conn -> query($sqlselect);
    if (mysqli_num_rows($execute)===1){
        $row = mysqli_fetch_assoc($execute);
        $ds['tenDangNhap'] = $row['tenDangNhap'];
        $ds['tenNV'] = $row['tenNV'];
        $ds['email'] =  $row['Email'];
        $ds['soDienThoai'] = $row['soDienThoai'];
        $ds['gioiTinh'] =  $row['gioiTinh'];
        $ds['matKhau']  = $row['matKhau'];
        $ds['maquyen'] = $row['maquyen'];
        array_push($result['data'], $ds);
        $result['status'] = 'success';
        header('Content-Type: application/json');
        echo json_encode($result);
    }   
 }      
?>