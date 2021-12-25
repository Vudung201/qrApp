<?php
if (isset($_POST['tenDangNhap']) && isset($_POST['matKhau'])) {
    require_once "connect.php";
    require_once "validate.php";
    $tenDangNhap = validate($_POST['tenDangNhap']);
    $matKhau = validate($_POST['matKhau']);
    $sql = "SELECT * FROM `nhanvien` WHERE tenDangNhap = '$tenDangNhap' and matKhau='$matKhau'";
    $result = $conn -> query($sql);
    if($result->num_rows >0){
        while($row = mysqli_fetch_assoc($result)){
            if ($row['maquyen'] == 1){
                echo "success1-".$row['maNV'];
            }else  if ($row['maquyen'] == 0){
                echo "success0-".$row['maNV'];
            }
        }
        
    }else{
        echo "failed";
    }
} 
