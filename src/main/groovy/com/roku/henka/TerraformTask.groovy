/*
* Copyright (c) 2016-2017, Henka Contributors
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with
* the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and limitations under the License.
*/
package com.roku.henka

import com.roku.henka.executors.TerraformExecutor
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Defines TerraforTask type. Properties (accepted on task):
 * <ul>
 *     <li> tfDir </li>
 *     <li> tfAction </li>
 *     <li> tfInitParams </li>
 *     <li> installTerraform </li>
 *     <li> terraformVersion </li>
 *     <li> terraformBaseDir </li>
 *  </ul>
 *
 */
@SuppressWarnings("GroovyUnusedDeclaration")
class TerraformTask extends DefaultTask {

    String tfDir
    String tfAction
    String tfInitParams = ""

    Boolean installTerraform = true
    String terraformVersion = "0.9.2"
    String terraformBaseDir = "/opt/terraform"

    /**
     * Parses properties and calls an appropriate TerraformExecutor, which is responsible for executing the
     * right Terraform command (plan, apply etc.)
     */
    @TaskAction
    def terraform() {
        validateProperties()
        if (installTerraform) {
            new TerraformInstaller().installTerraform(terraformBaseDir, terraformVersion)
        }

        TerraformExecutor tfExecutor
        if (installTerraform) {
            tfExecutor = new TerraformExecutor("$terraformBaseDir/$terraformVersion/")
        } else {
            tfExecutor = new TerraformExecutor()
        }
        tfExecutor.execute(this, tfAction, tfInitParams)
    }

    private void validateProperties() {
        if (tfDir == null) {
            throw new IllegalArgumentException("tfDir must be set")
        }

        if (tfAction == null) {
            throw new IllegalArgumentException("tfAction must be set")
        }

        if (tfInitParams == null) {
            throw new IllegalArgumentException("tfInitParams must be set")
        }

        if (installTerraform == null) {
            throw new IllegalArgumentException("installTerraform must be set")
        }

        if (installTerraform && terraformVersion == null) {
            throw new IllegalArgumentException("terraform must be set when installing Terraform")
        }

        if (installTerraform && terraformBaseDir == null) {
            throw new IllegalArgumentException("terraformBaseDir must be set when installing Terraform")
        }
    }

}
