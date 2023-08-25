package com.company;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Sistema {

    private List<Usuario>listaUsers=new ArrayList<>();
    private HashMap<String,Usuario>mapaUsuarios=new HashMap<>();
    private Usuario activeUser;
    private List<String>documentKeys=new ArrayList<>();
    private HashMap<Integer, Transaction>transactionToValidateMap=new HashMap<>();
    private HashMap<Integer,Transaction>blockchain=new HashMap<>();

//-------------------------------------------Constructor ---------------------------------------------------------------
    public Sistema() throws IOException {
        setPaths();
        loadUserFile();
        loadTransactionsToValidateFile();
        loadBlockchain();
        if(mapaUsuarios.size()<1){
            createSampleData();
        }
    }

//-----------------------------------------MANEJO ARCHIVOS--------------------------------------------------------------
    private String USER_PATH;
    private String USUARIOS_PATH;
    private String TRANSACTIONS_TO_VALIDATE_PATH;
    private String BLOCKCHAIN_PATH;
    //SISTEMA DEBERIA RECIBIR LA DATA DEL INGRESO POR TECLADO DEL USUARIO Y LUEGO HACER LAS VALIDACIONES. LUEGO SI PASA LAS VALIDACIONES SETEAR EL ACTIVE USER CON UN SetActiveUser

    public void createSampleData() throws IOException {
        try {
            Usuario uno = new Usuario("Gonzalo Orellano", "GonzaOrellano@hotmail.com", "Password1", "10000001");
            Usuario dos = new Usuario("Maximiliano Buono", "MaximoBuono@hotmail.com", "Password1", "10000002");
            Usuario tres = new Usuario("Gabriela Macchi", "GabrielaMacchi@hotmail.com", "Password1", "10000003");
            Usuario cuatro = new Usuario("Micaela Buono", "d@hotmail.com", "Password1", "10000004");
            Usuario cinco = new Usuario("Sherlock Holmes", "SherlockHolmes@hotmail.com", "Password1", "10000005");
            Usuario seis = new Usuario("Mariano Ramirez", "MarianoRamirez@hotmail.com", "Password1", "10000006");
            Usuario siete = new Usuario("Alan Gimenez", "AlanGimenez@hotmail.com", "Password1", "10000007");
            Usuario ocho = new Usuario("Leonardo Alonso", "LeonardoAlonso@hotmail.com", "Password1", "10000008");
            Usuario nueve = new Usuario("Laura Garcia", "LauraGarcia@hotmail.com", "Password1", "10000009");
            Usuario diez = new Usuario("Marcos fiszbein", "MarcosFiszbein@hotmail.com", "Password1", "10000010");
            Usuario once = new Usuario("Nicolas Ale", "NicolasAle@hotmail.com", "Password1", "40830432");

            mapaUsuarios.put(uno.getDni(), uno);
            mapaUsuarios.put(dos.getDni(), dos);
            mapaUsuarios.put(tres.getDni(), tres);
            mapaUsuarios.put(cuatro.getDni(), cuatro);
            mapaUsuarios.put(cinco.getDni(), cinco);
            mapaUsuarios.put(seis.getDni(), seis);
            mapaUsuarios.put(siete.getDni(), siete);
            mapaUsuarios.put(ocho.getDni(), ocho);
            mapaUsuarios.put(nueve.getDni(), nueve);
            mapaUsuarios.put(diez.getDni(), diez);
            mapaUsuarios.put(once.getDni(), once);


            crearHashMapArchivo(mapaUsuarios);
            setDocumentKeys();
            System.out.println("Usuarios cargados correctamente.");
        }catch(Exception e) {
            System.out.println("Error al cargar los usuarios default");
        }


    }
//----------------------------------------------USER OPERATIONS---------------------------------------------------------
    /*
    public void userOperationMakeTransaction() throws IOException {
        int amount=0;
        String document;
        boolean comp=false;
        Usuario recieber=new Usuario();
        Scanner scan = new Scanner(System.in);
        boolean salir=false;

        while (comp==false) {
            System.out.println("Ingrese el documento del usuario al que quiere enviar dinero \n");
            document = ingresarDNI();
            recieber = mapaUsuarios.get(document);

            if (recieber == null) {
                System.out.println("El documento ingresado no esta registrado en el sistema como un usuario.\n");
                salir=true;
            }else if(document.equals(activeUser.getDni())){
                System.out.println("No se puede enviar dinero a usted mismo... Deje de buscar bugs. Intentelo de nuevo \n");
                salir=true;
            }else{
                comp=true;
                System.out.println("Ingrese el Monto de la transferencia que quiere realizar \n");
                amount=scan.nextInt();
                if(amount>=50){
                    salir=false;
                    comp=true;
                }else{
                    System.out.println("No puede realizar una transferencia menor a 50 coins, intentelo de nuevo \n");
                    salir=true;
                    comp=false;
                }
                if(amount<=getActiveUser().getWallet().getUtnCoins())
                {
                    salir=false;
                    comp=true;
                }else{
                    System.out.println("No tenes saldo suficiente para realizar la transaccion");
                    salir=true;
                    comp=false;
                }
            }
            if(salir==true ) {
                System.out.println("[---------------------------------------------------------------------------]");
                System.out.println("¿Usted quiere intentar transferir otra vez?");
                System.out.println("1:Si.");
                System.out.println("2:No.");
                switch (ingresarOpcion()) {
                    case 1: {
                        comp =false;
                        salir=false;
                        break;
                    }
                    case 2: {
                        comp = true;
                        salir = true;
                        break;
                    }
                    default: {
                        System.out.println("[Opcion incorrecta]");
                        break;
                    }
                }
                System.out.println("[---------------------------------------------------------------------------]");
            }
        }

        if(salir==false) {
            generateNewTransaction(recieber, amount);
            System.out.println("La transaccion fue creada. Una vez validada el dinero sera enviado \n");
        }

        }*/
public void userOperationMakeTransaction() throws IOException {
    int amount=0;
    String document;
    boolean comp=false;
    Usuario recieber=new Usuario();
    Scanner scan = new Scanner(System.in);

    while (comp==false) {

        System.out.println("Ingrese el documento del usuario al que quiere enviar dinero \n");
        document = scan.nextLine();
        recieber = mapaUsuarios.get(document);
        if (recieber == null) {
            System.out.println("El documento ingresado no esta registrado en el sistema como un usuario, intentelo de nuevo \n");
        }else if(document.equals(activeUser.getDni())){
            System.out.println("No se puede enviar dinero a usted mismo... Deje de buscar bugs. Intentelo de nuevo \n");
        }
        else{
            comp=true;
            do {
                System.out.println("Ingrese el Monto de la transferencia que quiere realizar \n");
                amount = scan.nextInt();
                if (amount <= 10) {
                    System.out.println("No puede realizar una transferencia nula o con monto negativo, intentelo de nuevo \n");
                } else if (amount > this.activeUser.getWallet().getUtnCoins()) {
                    System.out.println("Saldo en la Wallet insuficiente\n");
                }
            }while (amount <= 10 || amount > this.activeUser.getWallet().getUtnCoins());
        }
    }
    generateNewTransaction(recieber,amount);
    System.out.println("La transaccion fue creada. Una vez validada los UTNcoins seran enviados \n");

}
    public void userOperationsValidateTransactions() throws IOException {
        System.out.println("\n \n \n \n \n \n \n \n \n \n \n \n  \n \n \n \n \n \n \n \n");

        Scanner scan = new Scanner(System.in);
        int option=4;
        while (option!=0) {
            System.out.println(" 1- Mostrar transacciones a validar \n 2- Validar transacciones \n 0- Volver a menu principal");
            option=scan.nextInt();
            switch (option) {
                case 1:
                    showTransactionsToValidate();
                    break;
                case 2:
                    validateTransactions();
                    break;
            }
        }
        System.out.println("\n \n \n \n \n \n \n \n \n \n \n \n  \n \n \n \n \n \n \n \n");
    }
    public void userOperationsShowProfile(){
        System.out.println("\n \n \n \n \n \n \n \n \n \n \n \n  \n \n \n \n \n \n \n \n");
        int option=5;
        Scanner scan=new Scanner(System.in);
            System.out.println("Perfil de " + activeUser.getNombre() + "\n");
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("Datos usuario: \n DNI: " + activeUser.getDni() + "\n Mail: " + activeUser.getMail() + "\n ID:" + activeUser.getId() + "\n");
            System.out.println("Wallet: \n " + activeUser.getWallet()+"\n");
        System.out.println("------------------------------------------------------------------------------------ \n");
            System.out.println("Presione 0 para volver al menu principal");
        while (option!=0) {
            option=scan.nextInt();
        }
        System.out.println("\n \n \n \n \n \n \n \n \n \n \n \n  \n \n \n \n \n \n \n \n");
    }
    public void userOperationsShowTransactionHistory(){
        ArrayList<Transaction>transactionsNotValidated=new ArrayList<>();
        ArrayList<Transaction>transactionsSuccess=new ArrayList<>();
        boolean successempty=true;
        boolean tovalidateEmpty=true;

        ///////////////////////////////////////////////////////////////////////////////////////
        ///Por cada ciclo obtenemos la referencia del dueño.                                ///
        ///El usuario que generamos obtiene todos los datos.                                ///
        ///Ese mismo lo usamos para comprar con el usuario Activo                           ///
        ///Si es igual, lo agrega a la lista de las transacciones realizadas.               ///
        ///Y successempity lo ponemos en false porque si existe al menos una transaccion.   ///
        ///////////////////////////////////////////////////////////////////////////////////////
        for (Map.Entry<Integer,Transaction>entry:blockchain.entrySet()) {
            Usuario sender= getUserByOwnerReference(entry.getValue().getSender().getOwnerReference());
            if(sender.getDni().equals(activeUser.getDni())){
                transactionsSuccess.add(entry.getValue());
                successempty=false;
            }
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////////
        ///Recorremos el mapa de transacciones que necesitan validacion.                                ///
        ///Por cada ciclo obtenemos la referencia del dueño.                                            ///
        ///El usuario que generamos obtiene todos los datos.                                            ///
        ///Ese mismo lo usamos para comprar con el usuario Activo                                       ///
        ///Si es igual, lo agrega a la lista de transacciones NO validadas.                             ///
        ///Y tovalidateEmpity lo ponemos en false porque si existe al menos una transaccion SIN VALIDAR ///
        ///////////////////////////////////////////////////////////////////////////////////////////////////
        for (Map.Entry<Integer,Transaction>entry:transactionToValidateMap.entrySet()) {
            Usuario sender= getUserByOwnerReference(entry.getValue().getSender().getOwnerReference());
            if(sender.getDni().equals(activeUser.getDni())){
                transactionsNotValidated.add(entry.getValue());
                tovalidateEmpty=false;
            }
        }

        System.out.println("[------------------ Historial de Transacciones ----------------------]\n");
        if(tovalidateEmpty ==false) {
            System.out.println("Transacciones esperando a ser validadas: \n");
            for (Transaction e:transactionsNotValidated) {
                System.out.println(e.toString()+"\n");
            }
        }else{
            System.out.println("No tiene transacciones esperando a ser validadas \n");
        }
        System.out.println("\n \n");

        if(successempty ==false){
            System.out.println("Transacciones validadas y subidas a la blockchain: \n");
            for (Transaction e:transactionsSuccess) {
                System.out.println(e.toString()+"\n");
            }
        }else{
            System.out.println("No tiene transacciones registradas en la blockchain \n");
        }

    }
//---------------------------------------------------MUESTRAS-----------------------------------------------------------

    public void Muestra1() throws IOException {

        System.out.println("Creamos 6 usuarios y los cargamos al Archivo de usuarios ");
        Usuario uno=new Usuario("Alan","alan@gmail","password","41079103");
        Usuario dos=new Usuario("Nico","nico@gmail","password2","31079103");
        Usuario tres=new Usuario("Santi","fgdf@gmail","password3","41459103");
        Usuario cuatro=new Usuario("fef","dfg@gmail","password4","1231231234");
        Usuario cinco=new Usuario("sasd","dfg@gmail","password5","546645645");
        Usuario seis=new Usuario("asease","dfgdf@gmail","password6","345345345");

        mapaUsuarios.put(cuatro.getDni(),cuatro);
        mapaUsuarios.put(dos.getDni(),dos);
        mapaUsuarios.put(tres.getDni(),tres);
        mapaUsuarios.put(uno.getDni(),uno);
        mapaUsuarios.put(cinco.getDni(),cinco);
        mapaUsuarios.put(seis.getDni(),seis);
        crearHashMapArchivo(mapaUsuarios);
        System.out.println("Se crearon los siguientes usuarios : "+mapaUsuarios);
        //---------------------//
        System.out.println("Se setea al primer usuario como el usuario Activo");
        this.activeUser=uno;
        System.out.println("El usuario activo es: "+activeUser.toString());
        //--------------------//
        System.out.println("Se generan 2 nuevas transacciones emitidas por el usuario Activo");
        setDocumentKeys();
        generateNewTransaction(uno,25);
        generateNewTransaction(uno,100);
        System.out.println("Listo, las transacciones se cargaron en el archivo de Transacciones por validar");
        //--------------------//
        System.out.println("Cada transaccion debe ser validada por 3 Usuarios, al dispararse la solicitud de validacion los 3 " +
                "           usuarios tienen 5 minutos para declinar o aceptar la transaccion a la blockchain, de pasarse ese tiempo" +
                "            el sistema vuelve a generar 3 validadores random dentro de la lista de usuarios quitando a quienes no respondieron a tiempo" +
                "               yapa: confirmar una transaccion otorga 50 UTNCoins  ");
        //-------------------//
        System.out.println("Para evitar este proceso acabamos de validar todas las transacciones ya que esto es una muestra :)");
        validateAllTransactions();
        addConfirmedTransactionsToBlockchain();
        System.out.println("Como puede ver las transacciones ya se añadieron a la blockchain y se eliminaron del archivo de transacciones por confirmar");

    }

//--------------------------------------------------BLOCKCHAIN ---------------------------------------------------------
    public void createBlockchainFile(HashMap<Integer, Transaction> mapT) throws IOException {
    File transactionsToValidatePath=new File(BLOCKCHAIN_PATH+"\\Blockchain.json");
    ObjectMapper mapper=new ObjectMapper();
    transactionsToValidatePath.delete();
    mapper.writeValue(transactionsToValidatePath,mapT);
}
    public void addConfirmedTransactionsToBlockchain() throws IOException {
            HashMap<Integer,Transaction>transactionsAUX=this.transactionToValidateMap;
            HashMap<Integer,Transaction>transactionsToDelete=this.transactionToValidateMap;
            ArrayList<Integer>integers=new ArrayList<>();


        for (Map.Entry<Integer, Transaction> entry : transactionsAUX.entrySet()) {
                boolean comp=false;
                for(Map.Entry<String,Boolean>validatorsEntry:entry.getValue().getValidators().entrySet()){
                    if(validatorsEntry.getValue()==false){
                        comp=true;
                    }
                }
                if(comp==false){
                    transactionsAUX.get(entry.getKey()).getValidators().remove(entry.getKey());
                    blockchain.put(entry.getKey(),entry.getValue());
                    integers.add(entry.getKey());
                    // Ustedes se preguntaran que rayos hice aca... yo me pregunto lo mismo, pero funciona
                     mapaUsuarios.get(getUserByOwnerReference(entry.getValue().getRecieber().getOwnerReference()).getDni()).getWallet().setUtnCoins(entry.getValue().getRecieber().getUtnCoins()+entry.getValue().getAmount());
                     mapaUsuarios.get(getUserByOwnerReference(entry.getValue().getSender().getOwnerReference()).getDni()).getWallet().setUtnCoins(entry.getValue().getSender().getUtnCoins() - entry.getValue().getAmount());
                }
            }
        deleteValidatedTransactions(integers);
        updateBlockchain();
        crearHashMapArchivo(mapaUsuarios);
    }
    public Usuario getUserByOwnerReference(int ownerReference){
        for (Map.Entry<String,Usuario>entry:mapaUsuarios.entrySet()) {
            if (entry.getValue().getWallet().getOwnerReference()==ownerReference){
                return mapaUsuarios.get(entry.getKey());
            }
        }
        return null;
    }
    public void deleteValidatedTransactions(ArrayList<Integer>integers) throws IOException {

    for (Integer e:integers) {
        this.transactionToValidateMap.remove(e);
    }
    createTransactionsToValidateFile(this.transactionToValidateMap);

}
    public void updateBlockchain() throws IOException {
    File blockchainPath=new File(BLOCKCHAIN_PATH+"\\Blockchain.json");
    ObjectMapper mapper=new ObjectMapper();
    blockchainPath.delete();
    mapper.writeValue(blockchainPath,blockchain);
}
    public void loadBlockchain() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        mapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        mapper.enable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);

        File file = new File(BLOCKCHAIN_PATH + "\\Blockchain.json");
        if(file.exists()){
            TypeReference<HashMap<Integer, Transaction>> typeRef
                    = new TypeReference<HashMap<Integer, Transaction>>() {
            };

            this.blockchain = mapper.readValue(file, typeRef);
        }else{
            createBlockchainFile(blockchain);
        }
    }


//-----------------------------------------------ACTIVE USER ACTIONS----------------------------------------------------
    public void showTransactionsToValidate(){
        boolean comp=false;
        int transactionsToValidate=0;
        for (Map.Entry<Integer, Transaction> entry : transactionToValidateMap.entrySet()) {
            for (Map.Entry<String,Boolean> transactionEntry : entry.getValue().getValidators().entrySet()) {
                if(transactionEntry.getKey().matches((activeUser.getDni())))
                {
                    if(transactionEntry.getValue()==false) {
                        transactionsToValidate = transactionsToValidate + 1;
                    }
                }
            }
        }
        if (transactionsToValidate==0){
            System.out.println("Usted no tiene transacciones por validar \n");
        }else{
            System.out.println("Usted tiene : "+transactionsToValidate+" transacciones para validar \n");
        }
    }
    public void validateTransactions() throws IOException {
        int numberOftransactionsValidated=0;
        HashMap<Integer,Transaction>transactionsAUX=this.transactionToValidateMap;
        ArrayList<Integer>ses=new ArrayList<>();

        for (Map.Entry<Integer, Transaction> entry : transactionToValidateMap.entrySet()) {
            for (Map.Entry<String,Boolean> transactionEntry : entry.getValue().getValidators().entrySet()) {
                if(transactionEntry.getKey().matches((activeUser.getDni())))
                {
                    if(transactionEntry.getValue()==false) {
                        ses.add(entry.getKey());
                        numberOftransactionsValidated = numberOftransactionsValidated + 1;
                    }
                }
            }
        }
        //Esto es por un problema de concurrencia al iterar sobre el maldito hashmap
        for (Integer e:ses
             ) {
            transactionsAUX.get(e).updateValidation(activeUser.getDni());
        }
        if (numberOftransactionsValidated==0){
            System.out.println("Usted no tiene transacciones por validar \n");
        }
        else{
            System.out.println("Usted Valido : "+numberOftransactionsValidated+" transacciones \n");
            System.out.println("Se añadieron: "+(2*numberOftransactionsValidated)+" UTN Coins a su billetera \n");
        }
        activeUser.getWallet().setUtnCoins(activeUser.getWallet().getUtnCoins()+(2*numberOftransactionsValidated));
        createTransactionsToValidateFile(transactionsAUX);
        addConfirmedTransactionsToBlockchain();

    }

//--------------------------------------------------ADMIN ACTIONS-------------------------------------------------------
    public void validateAllTransactions() throws IOException  {
    for (Map.Entry<Integer, Transaction> entry : transactionToValidateMap.entrySet()) {
        transactionToValidateMap.get(entry.getKey()).updateAllValidatorsToTrue();
    }
    createTransactionsToValidateFile(this.transactionToValidateMap);
    addConfirmedTransactionsToBlockchain();
}

//-----------------------------------------------GETTERS AND SETTERS----------------------------------------------------
    /*
   Setea los paths de las carpetas donde se guardara el archivo de Usuarios
   */
    private void setPaths()    {
        this.USER_PATH=System.getProperty("user.dir");
        this.USUARIOS_PATH=""+USER_PATH+"\\users";
        this.TRANSACTIONS_TO_VALIDATE_PATH=""+USER_PATH+"\\transactionsToValidate";
        this.BLOCKCHAIN_PATH=""+USER_PATH+"\\Blockchain";
    }
    public Usuario getActiveUser() {

        return activeUser;
    }
    public void setActiveUser(Usuario activeUser) {

        this.activeUser = activeUser;
    }
    public HashMap<String, Usuario> getMapaUsuarios() {

        return mapaUsuarios;
    }
    public void setMapaUsuarios(HashMap<String, Usuario> mapaUsuarios) {

        this.mapaUsuarios = mapaUsuarios;
    }
    public List<String> getDocumentKeys() {
        return documentKeys;
    }
    public void setDocumentKeys() {
        for (HashMap.Entry<String, Usuario> entry : mapaUsuarios.entrySet()) {
            documentKeys.add(entry.getValue().getDni());
        }
    }


//-------------------------------------------MANEJO HASHMAP USUARIOS----------------------------------------------------
    public void  crearHashMapArchivo(HashMap<String,Usuario>map) throws IOException {
        ObjectMapper mapper=new ObjectMapper();
        File mapPath=new File(USUARIOS_PATH+"\\HashMapUsuarios"+".json");
        if(mapPath.exists()){
            mapPath.delete();
        }
        mapper.writeValue(mapPath, map);
    }
    public HashMap<String,Usuario> cargarMapaUsuariosDeArchivo() throws IOException {
        ObjectMapper mapper=new ObjectMapper();
        File file=new File(USUARIOS_PATH+"\\HashMapUsuarios.json");
        TypeReference<HashMap<String,Usuario>> typeRef
                = new TypeReference<HashMap<String,Usuario>>() {};

        HashMap<String,Usuario> o = mapper.readValue(file, typeRef);
        return o;

    }
    public void addUserToHashMapFile(Usuario a) throws IOException {
        ObjectMapper mapper=new ObjectMapper();
        File file=new File(USUARIOS_PATH+"\\HashMapUsuarios.json");
        TypeReference<HashMap<String,Usuario>> typeRef
                = new TypeReference<HashMap<String,Usuario>>() {};
        file.delete();
        this.mapaUsuarios.put(a.getDni(),a);
        crearHashMapArchivo(mapaUsuarios);

    }
    public void deleteUserFromHashMap(Usuario a) throws IOException {
        mapaUsuarios.remove(a.getDni());
        File file=new File(USUARIOS_PATH+"\\HashMapUsuarios.json");
        TypeReference<HashMap<String,Usuario>> typeRef
                = new TypeReference<HashMap<String,Usuario>>() {};
        file.delete();
        crearHashMapArchivo(mapaUsuarios);
    }
    public void mostrarHashMapUsuarios(){
        for (HashMap.Entry<String, Usuario> entry : mapaUsuarios.entrySet()) {
            System.out.println("clave=" + entry.getKey() + ", valor=" + entry.getValue());
        }
    }
    public void updateUserFile() throws IOException {
        File userpath=new File(USUARIOS_PATH+"\\HashMapUsuarios.json");
        ObjectMapper mapper=new ObjectMapper();
        userpath.delete();
        mapper.writeValue(userpath,mapaUsuarios);
    }

    public void chargeUTNcoinsAllUser()throws IOException{
        for (HashMap.Entry<String, Usuario> entry : mapaUsuarios.entrySet()) {
            entry.getValue().getWallet().setUtnCoins(entry.getValue().getWallet().getUtnCoins()+1000);
        }
        updateUserFile();
    }

    public void loadUserFile() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        mapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        mapper.enable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);

        File file = new File(USUARIOS_PATH + "\\HashMapUsuarios.json");
        if (file.exists()) {
            TypeReference<HashMap<String, Usuario>> typeRef
                    = new TypeReference<HashMap<String, Usuario>>() {
            };

            this.mapaUsuarios = mapper.readValue(file, typeRef);
        }else{
            crearHashMapArchivo(mapaUsuarios);
        }
    }

//-----------------------------------------MANEJO ARCHIVO Transacciones-------------------------------------------------
    public void createTransactionsToValidateFile(HashMap<Integer, Transaction> mapT) throws IOException {
        File transactionsToValidatePath=new File(TRANSACTIONS_TO_VALIDATE_PATH+"\\HashMapTransactionsToValidate.json");
        ObjectMapper mapper=new ObjectMapper();
        transactionsToValidatePath.delete();
        mapper.writeValue(transactionsToValidatePath,mapT);
    }
    public void addTransactionToValidate(Transaction toAdd) throws IOException {
        ObjectMapper mapper=new ObjectMapper();
        File transactionsToValidatePath=new File(TRANSACTIONS_TO_VALIDATE_PATH+"\\HashMapTransactionsToValidate.json");
        TypeReference<HashMap<Integer,Transaction>> typeRef
                = new TypeReference<HashMap<Integer,Transaction>>() {};
        transactionsToValidatePath.delete();
        this.transactionToValidateMap.put(toAdd.getId(),toAdd);
        createTransactionsToValidateFile(transactionToValidateMap);
    }
    public void loadTransactionsToValidateFile() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        mapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        mapper.enable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);

        File file = new File(TRANSACTIONS_TO_VALIDATE_PATH + "\\HashMapTransactionsToValidate.json");
        if (file.exists()) {
            TypeReference<HashMap<Integer, Transaction>> typeRef
                    = new TypeReference<HashMap<Integer, Transaction>>() {
            };

            this.transactionToValidateMap = mapper.readValue(file, typeRef);
        }else{
            createTransactionsToValidateFile(transactionToValidateMap);
        }
    }
    public int transactionIdGenerator(){
        int id= transactionToValidateMap.size()+blockchain.size();
        id++;
        return id;
    }

    //TODO

///----------------------------------------------TRANSACTIONS-----------------------------------------------------------
    private void generateNewTransaction(Usuario recieber,int amount) throws IOException {
        Transaction newTransaction=new Transaction(recieber.getWallet(),activeUser.getWallet(),generateTransactionValidators(),amount,transactionIdGenerator());
        addTransactionToValidate(newTransaction);
    }
    private HashMap<String,Boolean> generateTransactionValidators(){
        //ACA TAMBIEN TENEMOS QUE RECIBIR EL USUARIO QUE CREA LA TRANSACCION Y EL USUARIO QUE LA RECIBE Y COMPROBAR QUE NO SEAN UNO DE LOS VALIDADORES

        ArrayList<Integer> numbers = new ArrayList<>();
        int validatorsQuantity=3;
        int i=0;

        /*
        Generamos 3 numeros random y los agregamos a la lista,se comprobamos que
        No se no se agreguen repetidos
        El usuario activo no sea 1 de los validadores

        */
        while  (i<validatorsQuantity){
            boolean comp=false;
            Random numAleatorio=new Random();
            int randomNumber= numAleatorio.nextInt(documentKeys.size());
            for(int e:numbers){
                if(randomNumber==e){
                    comp=true;
                }else if(randomNumber==activeUser.getId()){

                    comp=true;
                }
            }
            if(comp==false){
                numbers.add(randomNumber);
                i++;
            }
        }
        /*
         Cremos lista de usuarios y le pasamos a la transaccion que recibimos por parametro la lista con los usuarios seleccionados de forma random
        * */
        HashMap<String,Boolean>validators=new HashMap<>();
        for (Integer f:numbers){
            validators.put(mapaUsuarios.get(documentKeys.get(f)).getDni(),false);
        }
        return validators;
    }


    //-----------------------------------------Opciones de Login----------------------------------------------------------//
    public boolean login() {
        boolean confirmacion=false;
        System.out.println("[Para ingresar a tu cuenta necesitamos los siguientes datos]");
        String dniLogin = ingresarDNI();
        String passwordLogin = ingresarPassword();

        Usuario newUser = validUsuario(dniLogin, passwordLogin);
        if (newUser != null) {
            confirmacion=true;
            setActiveUser(newUser);
            setDocumentKeys();
        }else{
            System.out.println("Login Incorrecto");
        }

        return confirmacion;
    }

    public Usuario RegistroUsuario() throws IOException {
        int dia, mes, anio;
        String nombre,dni,email,password;
        Scanner input=new Scanner(System.in);
        boolean validarRegistro=false;
        Usuario newUser=null;
        System.out.println("[ Registro de Usuario ]");
        System.out.println("Bienvenido al banco, vamos a iniciar el registro completando los siguientes campos.");

        System.out.println("\n[Ingresar tu nombre y apellido completo]");
        nombre=ingresarUserName();
        System.out.println("\n[Ingresar tu email]");
        email=ingresarEmail();
        System.out.println("\n[Ingresar tu DNI]");
        dni=ingresarDNI();
        System.out.println("\n[Ingresar tu Password]");
        password=ingresarPassword();

        try{
            System.out.println("[Ingresar fecha de nacimiento]");
            System.out.println("Dia: ");
            dia=input.nextInt();
            System.out.println("Mes: ");
            mes=input.nextInt();
            System.out.println("Año: ");
            anio=input.nextInt();


            if(validAge18(LocalDate.of(anio,mes,dia))){
                if (existUsuario(dni)) {
                    System.out.println("El usuario con el dni "+dni+" ya existe.");
                    validarRegistro=false;
                } else {
                    System.out.println("Registro realizado correctamente, ya podes ingresar a tu cuenta.");
                    validarRegistro=true;
                }
            }else{
                System.out.println("Para registrarse necesitas ser mayor de edad.");
            }
        }   catch (Exception e) {
            System.out.println("Error! Fecha mal ingresada");
        }


        if(validarRegistro) {
            newUser=new Usuario(nombre,email,password,dni);
            mapaUsuarios.put(newUser.getDni(), newUser);
            try {
                addUserToHashMapFile(newUser);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            System.out.println("El usuario no se registro.");
        }
        return newUser;
    }

    //------------------------------------------MENU PRINCIPAL------------------------------------------------------------//

    public void opcionesMenuLogin() {
        System.out.println("\n[Login - Regist]");
        System.out.println(" 1 - Login.");
        System.out.println(" 2 - Registrarse");
        System.out.println(" 0 - Salir");
    }
    public void opcionesMenuPrincipal(Usuario activeUser) {
        if(activeUser!=null) {
            System.out.println("\n[ Bienvenido " + activeUser.getNombre() + " ]");
            System.out.println("1 - Transacciones");
            System.out.println("2 - Ver mi perfil");
            System.out.println("3 - Menu ADMIN");
            System.out.println("0 - Salir");
        }
    }

    public void opcionesMenuTransacciones(Usuario activeUser)
    {
        System.out.println("      [ Transacciones ]");
        System.out.println("1- Realizar Transaccion");
        System.out.println("2- Validar Transacciones");
        System.out.println("0 - Salir");
    }

    public void opcionesMenuMyPerfil()
    {
        System.out.println("[Base de datos de "+getActiveUser().getNombre()+"]");
        System.out.println("1- Mi perfil");
        System.out.println("2- Historial de transacciones");
        System.out.println("0- Salir");
    }
    public boolean opcionesMenuADMIN(String contraseña)
    {
        if(contraseña.equals("utn")) {
            System.out.println("[Panel de ADMIN]");
            System.out.println("1- Cargar UTN$1000 a todas las cuentas");
            System.out.println("2- Validar todas las transacciones");
            System.out.println("3- Ver Usuarios cargados en el sistema");
            System.out.println("0- Volver");

            return true;
        }
        else
        {
            System.out.println("Contraseña incorrecta");
            return false;
        }
    }

    //----------------------------------Validaciones Login y Registrp------------------------------------------------------//

    ///Creamos la validacion de edad de esta manera porque:

    public boolean validAge18(LocalDate fechaNacimiento)
    {
        boolean valido=false;
        Period edad = Period.between(fechaNacimiento, LocalDate.now());
        /*
        System.out.println(String.format("%d años, %d meses y %d días",
                edad.getYears(),
                edad.getMonths(),
                edad.getDays()));
        */

        if(edad.getYears()>=18)
        {
            valido=true;
        }
        return valido;
    }

    ///Arreglar esta funcion mas tarde (Funciona igual).
    public int ingresarOpcion()    {
        int opcion=666;
        Scanner input=new Scanner(System.in);
        try
        {
            System.out.println("Ingresar Opcion: ");
            opcion=input.nextInt();
        }catch (Exception e)
        {
            System.out.println("La opcion cargada genero un error.");
        }
        return opcion;
    }
    public Usuario validUsuario(String dni,String password)
    {
        Usuario retorno=null;

        for (HashMap.Entry<String, Usuario> entry : mapaUsuarios.entrySet()) {
            if(entry.getKey().equals(dni)) {
                if(entry.getValue().getContraseña().equals(password)) {
                    retorno=entry.getValue();
                } else {
                    System.out.println("La contraseña es incorrecta.");
                }
            }
        }
        return retorno;
    }
    public boolean existUsuario(String dni)
    {
       boolean retorno=false;

        for (HashMap.Entry<String, Usuario> entry : mapaUsuarios.entrySet()) {
            if (entry.getKey().equals(dni)) {
                retorno = true;
                break;
            }
            }
        return retorno;
    }

// --------------------------------------Validacion ingreso por teclado-------------------------------------------------//
    public String ingresarDNI() {
        String dni="Error";
        boolean inspector;
        Scanner scan = new Scanner(System.in);

        try {
            do {
                System.out.println(" DNI : ");
                dni = scan.nextLine();
                inspector = validDni(dni);
            } while (!inspector);
        }catch (Exception e)
        {
            System.out.println("Error fatal en el ingreso del DNI.");
        }

        return dni;
    }
    public String ingresarUserName() {
        String userName="Error";
        boolean inspector;
        Scanner scan = new Scanner(System.in);

        try {
            do {
                System.out.println(" Username : ");
                userName = scan.nextLine();
                inspector = validUsername(userName);
            } while (!inspector);
        }catch (Exception e)
        {
            System.out.println("Error fatal al ingresar el nombre de usuario.");
        }
        return userName;
    }
    public String ingresarEmail() {
        String emailNuevo="Error";
        boolean inspector;

        Scanner scan = new Scanner(System.in);

        try {
        do {
            System.out.println("Email: ");
            emailNuevo = scan.nextLine();
            inspector = validEmail(emailNuevo);
        } while (!inspector);
        }catch (Exception e)
        {
            System.out.println("Error fatal en el ingreso del Email");
        }

        return emailNuevo;
    }

    public String ingresarPassword() {
        String password="Error";
        boolean inspector;
        Scanner scan = new Scanner(System.in);

        try {
            do {
                System.out.println(" Password : ");
                password = scan.nextLine();
                inspector = validPassword(password);
            } while (!inspector);
        }catch (Exception e)
    {
        System.out.println("Error fatal en el ingreso del Email");
    }

        return password;
    }
    private boolean validDni(String dni) {
        int i, j = 0;
        String numero = ""; // Es el numero que se comprueba uno a uno por si hay alguna letra entre los 8 primeros digitos
        String[] unoNueve = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        for (i = 0; i < dni.length() - 1; i++) {
            numero = dni.substring(i, i + 1);

            for (j = 0; j < unoNueve.length; j++) {
                if (numero.equals(unoNueve[j])) {
                    numero += unoNueve[j];
                }
            }
        }

        if (dni.length() != 8) {
            System.out.println("El DNI ingresado es invalido.");
            return false;
        } else {
            return true;
        }
    }
    public static boolean validUsername(String name) {
        String specialChars = "~`!@#$%^&*()-_=+\\|[{]};:'\",<.>/?";
        boolean specialchar = false;
        boolean numero = false;
        if (name.length() > 4 && name.length() < 15) {
            for (int i = 0; i < name.length(); i++) {
                if (specialChars.contains(String.valueOf(name.charAt(i)))) {
                    specialchar = true;
                } else if (Character.isDigit(Integer.valueOf(name.charAt(i)))) {
                    numero = true;
                }
            }

            if (specialchar && numero) {
                System.out.println("El nombre " + name + " es invalido");
                return false;
            } else {
                return true;
            }
        } else {
            System.out.println("El nombre " + name + " no tiene caracteres suficientes.");
            return false;
        }
    }
    private boolean validPassword(String password) {
        final int MAX = 8;
        final int MIN_Uppercase = 1;
        final int MIN_Lowercase = 1;
        final int NUM_Digits = 0;
        int uppercaseCounter = 0;
        int lowercaseCounter = 0;
        int digitCounter = 0;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c))
                uppercaseCounter++;
            else if (Character.isLowerCase(c))
                lowercaseCounter++;
            else if (Character.isDigit(c))
                digitCounter++;
        }

        if (password.length() >= MAX && uppercaseCounter >= MIN_Uppercase && lowercaseCounter >= MIN_Lowercase && digitCounter >= NUM_Digits) {
            return true;
        } else {
            System.out.println(" Su contraseña no contiene lo siguiente:");
            if (password.length() < MAX)
                System.out.println(" [ Al menos 8 carácteres ]");
            if (lowercaseCounter < MIN_Lowercase)
                System.out.println(" [ Letras minúsculas ]");
            if (uppercaseCounter < MIN_Uppercase)
                System.out.println(" [ Letras mayúsculas ]");
            if (digitCounter < NUM_Digits)
                System.out.println(" [ Número mínimo de dígitos numéricos ]");

            return false;
        }
    }
    private boolean validEmail(String email) {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mather = pattern.matcher(email);

        if (mather.find()) {
            return true;
        } else {
            System.out.println("El email ingresado es inválido.");

            return false;
        }
    }

}
