chcp 1250
mvn clean install
if( Test-Path "target/wulan_back.war" -PathType Leaf){
  Copy-Item "target/wulan_back.war" -Destination "c:\Tomcat\webapps\"
}
else{
  Write-Host "Coœ nie tak z kompilacj¹"
  Pause
  EXIT
}
