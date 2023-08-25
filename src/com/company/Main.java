package com.company;

import javax.swing.*;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        Sistema sistem = new Sistema();
        int option = 1;
        Scanner scan = new Scanner(System.in);
        boolean confirmacion = false;
        boolean salir = false;

        do {
            sistem.opcionesMenuLogin();
            /////////////////////////////////////
            ///Menu - Login , Registro y Salir //
            /////////////////////////////////////

            switch (sistem.ingresarOpcion()) {
                case 1: {
                    /////////////////////////
                    ///Cargamos los files ///
                    /////////////////////////

                    sistem.loadUserFile();

                    do {
                        confirmacion = sistem.login();
                        if (confirmacion == false) {
                            System.out.println("¿Usted quiere intentar conectarse otra vez?");
                            System.out.println("1:Si.");
                            System.out.println("2:No.");
                            switch (sistem.ingresarOpcion()) {
                                case 1: {
                                    confirmacion = false;
                                    break;
                                }
                                case 2: {
                                    confirmacion = true;
                                    sistem.setActiveUser(null);
                                    salir = true;
                                    break;
                                }
                                default: {
                                    System.out.println("[Opcion incorrecta, rehubicado al menu de login]");
                                    confirmacion = false;
                                    break;
                                }
                            }
                        } else {
                            System.out.println("Ingreso correctamente, Bienvenido a nuestro banco " + sistem.getActiveUser().getNombre());
                            salir = true;
                            option = 0;
                        }
                    } while (confirmacion != true || salir != true);
                    break;
                }
                case 2: {
                    System.out.println("[Registro de Usuario]");
                    Usuario confirmacionRegistro = null;
                    confirmacionRegistro = sistem.RegistroUsuario();
                    if (confirmacionRegistro != null) {
                        sistem.setActiveUser(confirmacionRegistro);
                        option = 0;
                    }
                    //Si el retorno del registro es exitoso usuario se logea, De lo contrario el usuario es null.
                    //El usuario se guarda en el File "HashmapUsuarios" cuando se registra.
                    break;
                }
                case 0: {
                    System.out.println("Gracias por usar nuestro programa.");
                    sistem.setActiveUser(null);
                    option = 0;
                    break;
                }
                default: {
                    System.out.println("-> Opcion incorrecta <-");
                    break;
                }
            }
        } while (option != 0);


        if (sistem.getActiveUser() != null) {
            sistem.loadBlockchain();
            sistem.loadTransactionsToValidateFile();
            ////////////////////////////////////////
            //Reinicio de las variables booleans ///
            ////////////////////////////////////////
            salir = false;                        ///
            confirmacion = false;                 ///
            option = 666;                         ///
            ////////////////////////////////////////
            do {
                sistem.opcionesMenuPrincipal(sistem.getActiveUser());
                //////////////////////////////////////////////////////////////////
                ///Menu principal: 1-Transacciones 2-Mi perfil 3-Menu Admin     //
                //////////////////////////////////////////////////////////////////
                switch (sistem.ingresarOpcion()) {
                    case 1: {
                        ////////////////////////////////////////
                        //Reinicio de las variables booleans ///
                        ////////////////////////////////////////
                        salir = false;                       ///
                        confirmacion = false;                ///
                        option = 666;                        ///
                        ////////////////////////////////////////
                        do {
                            sistem.opcionesMenuTransacciones(sistem.getActiveUser());
                            //////////////////////////////////////////////////////////////////////////////
                            ///Menu transacciones:                                                      //
                            ///1-Hacer una transaccion ,2-Validar Transacciones                         //
                            ///3-Depositar, 4-Comprar UTNCoins                                          //
                            //////////////////////////////////////////////////////////////////////////////
                            switch (sistem.ingresarOpcion()) {
                                case 1: {
                                    System.out.println("[Realizar Transaccion]");
                                    sistem.userOperationMakeTransaction();
                                    break;
                                }
                                case 2: {
                                    System.out.println("[Transacciones para validar]");
                                    sistem.userOperationsValidateTransactions();
                                    break;
                                }

                                case 0: {
                                    System.out.println("Gracias por usar nuestro programa.");
                                    salir = true;
                                    option = 666;
                                    break;
                                }
                                default: {
                                    System.out.println("Opcion Incorrecta");
                                    break;
                                }
                            }
                        } while (salir != true);
                        break;
                    }

                    case 2: {
                        ////////////////////////////////////////
                        //Reinicio de las variables booleans ///
                        ////////////////////////////////////////
                        salir = false;                       ///
                        confirmacion = false;                ///
                        option = 666;                        ///
                        ////////////////////////////////////////
                        do {
                            //////////////////////////////////////////////
                            //Menu datos usuario:                       //
                            //1-Mi perfil , 2-Historial de transacciones//
                            //////////////////////////////////////////////
                            sistem.opcionesMenuMyPerfil();
                            switch (sistem.ingresarOpcion()) {
                                case 1: {
                                    sistem.userOperationsShowProfile();
                                    break;
                                }
                                case 2: {
                                    ///Tira error
                                    sistem.userOperationsShowTransactionHistory();
                                    break;
                                }
                                case 0: {
                                    salir = true;
                                    option = 666;
                                    break;
                                }
                                default: {
                                    System.out.println("Opcion incorrecta");
                                    break;
                                }
                            }
                        } while (salir != true);
                        break;
                    }

                    case 3: {
                        ////////////////////////////////////////
                        //Reinicio de las variables booleans ///
                        ////////////////////////////////////////
                        salir = false;                       ///
                        confirmacion = false;                ///
                        option = 666;                        ///
                        ////////////////////////////////////////
                        System.out.println("Ingresar la contraseña del Administrador:");
                        Scanner input = new Scanner(System.in);
                        String contraseña = input.nextLine();
                        //////////////////////////////////////////
                        //Menu de admin:                        //
                        //1-Cargar base de datos default        //
                        //2-Agregar $1000 a todas las cuentas   //
                        //3-Validar todas las transacciones     //
                        //////////////////////////////////////////
                        if (sistem.opcionesMenuADMIN(contraseña)) {
                            do {
                                switch (sistem.ingresarOpcion()) {

                                    case 1: {
                                        System.out.println("[Agregar UTN$1000 a todas las cuentas]");
                                        sistem.chargeUTNcoinsAllUser();
                                        break;
                                    }
                                    case 2: {
                                        System.out.println("[Validar todas las transacciones]");
                                        ///Tira error
                                        sistem.validateAllTransactions();
                                        break;
                                    }
                                    case 3: {
                                        System.out.println("[Mapa de usuarios]");
                                        sistem.mostrarHashMapUsuarios();
                                        break;
                                    }
                                    case 0: {
                                        salir = true;
                                        option = 666;
                                        break;
                                    }
                                    default: {
                                        System.out.println("Opcion incorrecta.");
                                        break;
                                    }
                                }
                            } while (salir != true);
                        } else {

                        }
                        break;
                    }

                    case 0: {
                        System.out.println("Gracias por usar nuestro programa.");
                        option = 0;
                        break;
                    }
                    default: {
                        System.out.println("Opcion Incorrecta.");
                        break;
                    }

                }

            } while (option != 0);
        }
    }

}

