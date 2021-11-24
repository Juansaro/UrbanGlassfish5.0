function acceder(){

if (document.form.password.value=='123456' && document.form.login.value=='recepcionista'){ 
        document.form.submit();
    }
    else{
        alert("Por favor ingrese nombre de usuario y contraseña correctos."); 
    }
}