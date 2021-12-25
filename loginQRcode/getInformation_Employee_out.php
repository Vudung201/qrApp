<?php
$connect = mysqli_connect("localhost", "root","","quanlynv");

$maNV = $_POST['maNV'];
mysqli_query($connect, "SET NAMES 'utf8'");
$query = "SELECT c.id, c.maNV, n.tenNV, c.ngayVao, c.gioVao FROM `quanlyvao`as c, `nhanvien` as n WHERE c.maNV = n.maNV AND c.maNV = '".$maNV."'";
$data = mysqli_query($connect, $query);
class Employee{
    function Employee($id, $maNV, $tenNV, $ngayVao, $gioVao){
        $this->Id = $id;
        $this-> MaNV = $maNV;
        $this -> TenNV = $tenNV;
        $this->NgayVao = $ngayVao;
        $this ->GioVao = $gioVao;
    }
}
// 2. Tạo mảng
$mangEp = array();
while($row = mysqli_fetch_assoc($data)){
    array_push($mangEp, new Employee($row['id'], $row[ 'maNV'],$row['tenNV'], $row['ngayVao'], $row['gioVao']));
}
// 4. Chuyển định dạng của mảng -> JSON
echo json_encode($mangEp);
?>