/*
 * Copyright 2013 Heiko Seeberger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package name.heikoseeberger.sbtgroll

import org.eclipse.jgit.api.{ Git => JGit, ResetCommand }
import org.eclipse.jgit.lib.Repository
import scala.collection.JavaConversions.iterableAsScalaIterable

class Git(repository: Repository) {

  private val jgit = new JGit(repository)

  def history(ref: String = "master"): Seq[(String, String)] = {
    val id = repository.getRef(ref).getObjectId
    jgit.log.add(id).call().toList map (commit => commit.shortId -> commit.getShortMessage)
  }

  def resetHard(ref: String = "master"): Unit =
    jgit.reset.setMode(ResetCommand.ResetType.HARD).setRef(ref).call()

  def clean(): Unit =
    jgit.clean.setCleanDirectories(true).setIgnore(true).call()
}
