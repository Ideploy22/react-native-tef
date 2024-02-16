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

import com.elgin.e1.pagamentos.tef.*

enum class TefWhat(val value: Int) {
    PROGRESS(1),
    COLLECT(2),
    TRANSACTION(3),
    INFO(4),
    FINISH(5),
    INFOPIX(6)
}

@ReactModule(name = IdeployTefModule.NAME)
class IdeployTefModule(reactContext: ReactApplicationContext) :
  NativeIdeployTefSpec(reactContext) {

  // val tag = "IDeploy: "
  val context = reactContext as ReactContext

  override fun getName(): String {
    return NAME
  }

  private val mHandler = object : Handler(Looper.getMainLooper()) {
    override fun handleMessage(msg: Message) {
      var value: String? = null
      val params: WritableMap = Arguments.createMap()
      params.putInt("what", msg.what)

      try {
        when (msg.what) {
          TefWhat.PROGRESS.value -> {
              value = ElginTef.ObterMensagemProgresso()
          }
          TefWhat.COLLECT.value -> {
              value = ElginTef.ObterOpcaoColeta()
          }
          TefWhat.TRANSACTION.value -> {
              value = ElginTef.ObterMensagemProgresso()
          }
            TefWhat.INFO.value -> {
              value = ElginTef.ObterDadosTransacao()
          }
          TefWhat.FINISH.value -> {
              value = ElginTef.ObterMensagemProgresso()
          }
          TefWhat.INFOPIX.value -> {
              value = msg.obj.toString()
          }
        }

        params.putString("message", value ?: "${msg.obj}")
        sendEvent(params)
      } catch (e: Exception) {
        params.putString("message", e.message)
        sendEventErro(params)
      }
    }
  }



  override fun onInitTef() {
    val activity: Activity = getCurrentActivity()!!

    ElginTef.setContext(activity)
    ElginTef.setHandler(mHandler)
  }

  override fun configTef(name: String, version: String, pinpad: String, doc: String) {
    ElginTef.InformarDadosAutomacao(name, version, pinpad,  "", "", "")

    try{
      ElginTef.AtivarTerminal(doc)
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

  override fun onCancel(){
    try{
      ElginTef.RealizarCancelamentoOperacao()
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
