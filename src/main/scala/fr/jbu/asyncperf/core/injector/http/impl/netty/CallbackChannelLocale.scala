package fr.jbu.asyncperf.core.injector.http.impl.netty

import org.jboss.netty.channel.ChannelLocal
import fr.jbu.asyncperf.core.injector.http.{HttpRequest, HttpResponse}
import fr.jbu.asyncperf.core.injector.InjectorRequest

class CallbackChannelLocale extends ChannelLocal[Tuple3[InjectorRequest[HttpRequest], Long, Function3[InjectorRequest[HttpRequest], Option[HttpResponse], Long, Unit]]] {

}

object ChannelLocaleContext{

  private val context = new CallbackChannelLocale

  def getContext:CallbackChannelLocale = {
    context
  }

}