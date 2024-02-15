package br.com.ideploy.tef

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext

import android.os.Handler
import android.app.Activity
import android.os.Message
import android.os.Looper
import android.widget.Toast

import com.elgin.e1.pagamentos.tef.ElginTef;

@ReactModule(name = IdeployTefModule.NAME)
class IdeployTefModule(reactContext: ReactApplicationContext) :
  NativeIdeployTefSpec(reactContext) {

  val context = reactContext as ReactContext

  override fun getName(): String {
    return NAME
  }

  private val mHandler = object : Handler(Looper.getMainLooper()) {
    override fun handleMessage(msg: Message) {
      var value: String? = null
      var valueEgin: String? = null

      try {
        value = msg.obj.toString()
        val params: WritableMap = Arguments.createMap()
        params.putString("message", value)

        if (msg.what == 1) { // MENSAGEM DE PROGRESSO
          params.putString("type", "progress")
          valueEgin = ElginTef.ObterMensagemProgresso()
          params.putString("messageEgin", valueEgin)
          sendEvent(params)
        } else if (msg.what == 2) { // OPÇÃO DE COLETA
          params.putString("type", "option")
          valueEgin = ElginTef.ObterOpcaoColeta()
          params.putString("messageEgin", valueEgin)
          sendEvent(params)
        } else if (msg.what == 3) { // DADOS DA TRANSAÇÃO
          params.putString("type", "transaction")
          valueEgin = ElginTef.ObterDadosTransacao()
          sendEvent(params)
        } else if (msg.what == 4) { // FINALIZAR
          params.putString("type", "finish")
          valueEgin = ElginTef.ObterDadosTransacao()
          sendEvent(params)
        } else {
          params.putString("type", "ELSE")
          sendEvent(params)
        }
      } catch (e: Exception) {

      }
      // Adiciona a opção de cancelamento ao builder. Deve ser possível cancelar na maioria das operações.
      // A situação de captura de DADOS_TRANSACAO é a única a retornar um AlerDialog.Builder null, portanto apenas se o valor retornando não for nulo deve ser mostrado o alert.
    }
  }

  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  // override fun multiply(a: Double, b: Double): Double {
  //   return a * b
  // }

  override fun onInitTef() {
    val activity: Activity = getCurrentActivity()!!

    ElginTef.setContext(activity)
    ElginTef.setHandler(mHandler)
  }

  override fun configTef(name: String, version: String, pinpad: String, pinPadText: String, doc: String) {
    ElginTef.InformarDadosAutomacao(name, version, pinpad, pinPadText)

    try{
      ElginTef.AtivarTerminal(doc);
    }catch(e: Exception){
      val params: WritableMap = Arguments.createMap()
      params.putString("message", e.message)
      sendEventErro(params)
    }
  }

  override fun payDeb(value: String) {
    try{
      ElginTef.RealizarTransacaoDebito(value)
    }catch(e: Exception){
      val params: WritableMap = Arguments.createMap()
      params.putString("message", e.message)
      sendEventErro(params)
    }
  }

  override fun payCred(value: String, type: String, instalments: String) {
    try{
      ElginTef.RealizarTransacaoCredito(value, type, instalments)
    }catch(e: Exception){
      val params: WritableMap = Arguments.createMap()
      params.putString("message", e.message)
      sendEventErro(params)
    }
  }

  override fun payPix(value: String) {
    try{
      ElginTef.RealizarTransacaoPIX(value)
    }catch(e: Exception){
      val params: WritableMap = Arguments.createMap()
      params.putString("message", e.message)
      sendEventErro(params)
    }
  }

  private fun sendEvent(params: WritableMap?) {
    context
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
      .emit("ideploy.tef", params)
  }

  private fun sendEventErro(params: WritableMap?) {
    context
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
      .emit("ideploy.tef.erro", params)
  }

  companion object {
    const val NAME = "IdeployTef"
  }
}
