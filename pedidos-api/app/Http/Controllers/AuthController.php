<?php

namespace App\Http\Controllers;

use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;


/**
 * @OA\OpenApi(
 *     @OA\Info(
 *         title="API de pedidos",
 *         version="1.0.0"
 *     )
 * )
 */
class AuthController extends Controller
{

    // documnetamos con swagger
    /**
     * @OA\Post(
     *    path="/api/login",
     *   summary="Login",
     *  tags={"Auth"},
     * @OA\RequestBody(
     *   required=true,
     *  description="Datos para loguearse",
     * @OA\JsonContent(
     *   required={"email","password"},
     * @OA\Property(property="email", type="string", format="email", example="
     *
     * "),
     * @OA\Property(property="password", type="string", format="password", example="
     *
     * "),
     * ),
     * ),
     * @OA\Response(
     *  response=200,
     * description="Usuario logueado correctamente",
     * @OA\JsonContent(
     * @OA\Property(property="success", type="boolean", example="true"),
     * @OA\Property(property="message", type="string", example="Usuario logueado correctamente"),
     * @OA\Property(property="data", type="object",
     * @OA\Property(property="id", type="integer", example="1"),
     * @OA\Property(property="name", type="string", example="Juan"),
     * @OA\Property(property="last_name", type="string", example="Perez"),
     * @OA\Property(property="email", type="string", example="
     *
     * "),
     * @OA\Property(property="is_admin", type="integer", example="1"),
     * @OA\Property(property="created_at", type="string", example="2021-09-01T00:00:00.000000Z"),
     * @OA\Property(property="updated_at", type="string", example="2021-09-01T00:00:00.000000Z"),
     * ),
     * ),
     * ),
     * @OA\Response(
     * response=404,
     * description="Usuario no encontrado",
     * @OA\JsonContent(
     * @OA\Property(property="success", type="boolean", example="false"),
     * @OA\Property(property="message", type="string", example="Usuario no encontrado"),
     * ),
     * ),
     * @OA\Response(
     * response=401,
     * description="Contraseña incorrecta",
     * @OA\JsonContent(
     * @OA\Property(property="success", type="boolean", example="false"),
     * @OA\Property(property="message", type="string", example="Contraseña incorrecta"),
     * ),
     * ),
     * )
     */
    public function login(Request $request) {

        $validate = validateRequestParams($request, [
            'email' => 'required|string',
            'password' => 'required|string',
        ]);

        if (!$validate['success'])
            return validateResponseError($validate);

        $user = User::where('email', $request->email)->first();

        if(!$user)
            return response()->json([
                'success' => false,
                'message' => 'Usuario no encontrado',
            ], 404);

        if(!Hash::check($request->password, $user->password))
            return response()->json([
                'success' => false,
                'message' => 'Contraseña incorrecta',
            ], 401);

        $token = $user->createToken('auth_token')->plainTextToken;
        $cookie = cookie('auth_token', $token, 60 * 24); // 1 day

        return response()->json([
            'success' => true,
            'message' => 'Usuario logueado correctamente',
            'data' => $user,
            'token' => $token,
        ])->withCookie($cookie);


    }

    // documnetamos con swagger
    /**
     * @OA\Post(
     *    path="/api/signup",
     *   summary="Registro",
     *  tags={"Auth"},
     * @OA\RequestBody(
     *   required=true,
     *  description="Datos para registrarse",
     * @OA\JsonContent(
     *   required={"name","email","password"},
     * @OA\Property(property="name", type="string", example="Juan"),
     * @OA\Property(property="last_name", type="string", example="Perez"),
     * @OA\Property(property="email", type="string", format="email", example="
     *
     * "),
     * @OA\Property(property="password", type="string", format="password", example="
     *
     * "),
     * ),
     * ),
     * @OA\Response(
     *  response=201,
     * description="Usuario creado correctamente",
     * @OA\JsonContent(
     * @OA\Property(property="success", type="boolean", example="true"),
     * @OA\Property(property="message", type="string", example="Usuario creado correctamente"),
     * ),
     * ),
     * @OA\Response(
     * response=400,
     * description="El email ya se encuentra registrado",
     * @OA\JsonContent(
     * @OA\Property(property="success", type="boolean", example="false"),
     * @OA\Property(property="message", type="string", example="El email ya se encuentra registrado"),
     * ),
     * ),
     * @OA\Response(
     * response=500,
     * description="Error al crear el usuario",
     * @OA\JsonContent(
     * @OA\Property(property="success", type="boolean", example="false"),
     * @OA\Property(property="message", type="string", example="Error al crear el usuario"),
     * ),
     * ),
     * )
     */
    public function signup(Request $request) {

        $validate = validateRequestParams($request, [
            'name' => 'required|string',
            'email' => 'required|string|unique:users',
            'password' => 'required|string|min:8',
        ]);

        if (!$validate['success'])
            return validateResponseError($validate);

        $user = User::where('email', $request->email)->first();
        if($user)
            return response()->json([
                'success' => false,
                'message' => 'El email ya se encuentra registrado',
            ], 400);

        $user = new User($request->all());
        $user->password = Hash::make($request->password);
        if($request->last_name){
            $user->last_name = $request->last_name;
            $user->is_admin = 1;
        }

        if(!$user->save())
            return response()->json([
                'success' => false,
                'message' => 'Error al crear el usuario',
            ], 500);

        return response()->json([
            'success' => true,
            'message' => 'Usuario creado correctamente'
        ], 201);


    }

    // documnetamos con swagger
    /**
     * @OA\Get(
     *    path="/api/users",
     *   summary="Obtener usuarios",
     *  tags={"Auth"},
     * @OA\Response(
     *  response=200,
     * description="Usuarios obtenidos correctamente",
     * @OA\JsonContent(
     * @OA\Property(property="success", type="boolean", example="true"),
     * @OA\Property(property="message", type="string", example="Usuarios obtenidos correctamente"),
     * @OA\Property(property="data", type="array",
     * @OA\Items(
     * @OA\Property(property="id", type="integer", example="1"),
     * @OA\Property(property="name", type="string", example="Juan"),
     * @OA\Property(property="last_name", type="string", example="Perez"),
     * @OA\Property(property="email", type="string", example="
     *
     * "),
     * @OA\Property(property="is_admin", type="integer", example="1"),
     * @OA\Property(property="created_at", type="string", example="2021-09-01T00:00:00.000000Z"),
     * @OA\Property(property="updated_at", type="string", example="2021-09-01T00:00:00.000000Z"),
     * ),
     * ),
     * ),
     * ),
     * ),
     * )
     */
    public function index(){
        $users = User::where('is_admin', 1)->get();

        return response()->json([
            'success' => true,
            'message' => 'Usuarios obtenidos correctamente',
            'data' => $users
        ], 200);
    }
}
